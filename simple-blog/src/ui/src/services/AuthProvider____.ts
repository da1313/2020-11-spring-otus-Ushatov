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
    return _userId;
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

  let observers: Array<(auth: {isLogged: boolean, userId: String | null}) => void> = [];

  const subscribe = (observer: (auth: {isLogged: boolean, userId: String | null}) => void) => {
    observers.push(observer);
  };

  const unsubscribe = (observer: (auth: {isLogged: boolean, userId: String | null}) => void) => {
    observers = observers.filter((o) => o !== observer);
  };

  const notify = () => {
    const isLogged = isLoggedIn();
    const userId = getUserId();
    observers.forEach((o) => o({isLogged: isLogged, userId: userId}));
  };

  const setToken = (token: typeof _token | null) => {
    let userId = null;
    if (token) {
      localStorage.setItem("REACT_TOKEN_AUTH", JSON.stringify(token));
      userId = extractUserId();
    } else {
      localStorage.removeItem("REACT_TOKEN_AUTH");
    }
    _token = token;
    _userId = userId;
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
    const [auth, setLogged] = useState({isLogged: tokenProvider.isLoggedIn(), userId: tokenProvider.getUserId()});

    useEffect(() => {
      const listener = (newAuth: {isLogged: boolean, userId: String | null}) => {
        setLogged(newAuth);
      };

      tokenProvider.subscribe(listener);
      return () => {
        tokenProvider.unsubscribe(listener);
      };
    }, []);

    return [auth] as [typeof auth];
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
