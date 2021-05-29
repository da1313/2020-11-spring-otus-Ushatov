import { useEffect, useState } from "react";

const createTokenProvider = () => {
  const loadFromStorage = (): {
    accessToken: string;
    refreshToken: string;
  } | null => {
    try {
      return JSON.parse(localStorage.getItem("REACT_TOKEN_AUTH") || "null");
    } catch (e) {
      return null;
    }
  };

  let _token = loadFromStorage();

  const extractUserId = ():String | null => {
    if (_token === null) return null;

    const split = _token.accessToken.split(".");

    if (split.length < 2) {
      return null;
    }

    try {
      const jwt = JSON.parse(atob(split[1]));
      return jwt.sub;
    } catch (e) {
      return null;
    }
  };

  let _userId = extractUserId();

  const getUserId = ():String|null =>{
    return extractUserId();
  }

  const isLoggedIn = () => {
    return !!_token;
  };

  const getExpirationDate = (token?: any): number | null => {
    if (!(typeof token === "string")) {
      return null;
    }

    const split = token.split(".");

    if (split.length < 2) {
      return null;
    }

    try {
      const jwt = JSON.parse(atob(split[1]));
      if (jwt && jwt.exp && Number.isFinite(jwt.exp)) {
        return jwt.exp * 1000;
      } else {
        return null;
      }
    } catch (e) {
      return null;
    }
  };

  const isExpired = (exp: number | null): boolean => {
    if (!exp) {
      return false;
    }

    return Date.now() > exp;
  };

  let observers: Array<(isLogged: boolean) => void> = [];

  const subscribe = (observer: (isLogged: boolean) => void) => {
    observers.push(observer);
  };

  const unsubscribe = (observer: (isLogged: boolean) => void) => {
    observers = observers.filter((o) => o !== observer);
  };

  const notify = () => {
    const isLogged = isLoggedIn();
    observers.forEach((o) => o(isLogged));
  };

  const setToken = (token: typeof _token | null) => {
    if (token) {
      localStorage.setItem("REACT_TOKEN_AUTH", JSON.stringify(token));
      _userId = extractUserId();
    } else {
      localStorage.removeItem("REACT_TOKEN_AUTH");
      _userId = null;
    }
    _token = token;
    notify();
  };

  const getToken = async (): Promise<String> => {
    if (!_token) {
      return "";
    }

    if (isExpired(getExpirationDate(_token.accessToken))) {
      const updateToken = await fetch("/update-token", {
        method: "POST",
        body: _token.refreshToken,
      })
        .then((r) => r.json())
        .catch(() => null); ///

      if (typeof updateToken !== typeof _token) {
        setToken(null);
      }

      setToken(updateToken);
    }

    return _token.accessToken;
  };

  return {
    getToken,
    isLoggedIn,
    setToken,
    subscribe,
    unsubscribe,
    getUserId,
  };
};

export const createAuthProvider = () => {
  const tokenProvider = createTokenProvider();

  const login: typeof tokenProvider.setToken = (newToken) => {
    tokenProvider.setToken(newToken);
  };

  const logout = () => {
    tokenProvider.setToken(null);
  };

  const authFetch = async (
    input: RequestInfo,
    init?: RequestInit
  ): Promise<Response> => {
    const token = await tokenProvider.getToken();

    init = init || {};

    if (token === "") {
      return fetch(input, init);
    }

    init.headers = {
      ...init.headers,
      Authorization: `${token}`,
    };

    return fetch(input, init);
  };

  const useAuth = () => {
    const [isLogged, setLogged] = useState(tokenProvider.isLoggedIn());

    useEffect(() => {
      const listener = (newIsLogged: boolean) => {
        setLogged(newIsLogged);
      };

      tokenProvider.subscribe(listener);
      return () => {
        tokenProvider.unsubscribe(listener);
      };
    }, []);

    return [isLogged] as [typeof isLogged];
  };

  const getUserId = ():String|null => {
    return tokenProvider.getUserId();
  };

  return {
    useAuth,
    authFetch,
    login,
    logout,
    getUserId,
  };
};

export const { useAuth, authFetch, login, logout, getUserId } = createAuthProvider();
