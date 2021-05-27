(this.webpackJsonpui=this.webpackJsonpui||[]).push([[0],{447:function(e,t,n){},449:function(e,t,n){},639:function(e,t,n){},641:function(e,t,n){"use strict";n.r(t);var r=n(0),i=n.n(r),c=n(18),s=n.n(c),o=(n(447),n(20)),a=n(429),l=n(19),j=n(93),u=n(14),d=n(77),b=n.n(d),h=n(132),O=function(){var e=function(){try{return JSON.parse(localStorage.getItem("REACT_TOKEN_AUTH")||"null")}catch(e){return null}}(),t=function(){if(null===e)return null;var t=e.accessToken.split(".");if(t.length<2)return null;try{return JSON.parse(atob(t[1])).sub}catch(n){return null}},n=(t(),function(){return!!e}),r=function(e){if("string"!==typeof e)return null;var t=e.split(".");if(t.length<2)return null;try{var n=JSON.parse(atob(t[1]));return n&&n.exp&&Number.isFinite(n.exp)?1e3*n.exp:null}catch(r){return null}},i=[],c=function(r){r?(localStorage.setItem("REACT_TOKEN_AUTH",JSON.stringify(r)),t()):(localStorage.removeItem("REACT_TOKEN_AUTH"),null),e=r,function(){var e=n();i.forEach((function(t){return t(e)}))}()};return{getToken:function(){var t=Object(h.a)(b.a.mark((function t(){var n;return b.a.wrap((function(t){for(;;)switch(t.prev=t.next){case 0:if(e){t.next=2;break}return t.abrupt("return","");case 2:if(!((i=r(e.accessToken))&&Date.now()>i)){t.next=8;break}return t.next=5,fetch("/update-token",{method:"POST",body:e.refreshToken}).then((function(e){return e.json()})).catch((function(){return null}));case 5:typeof(n=t.sent)!==typeof e&&c(null),c(n);case 8:return t.abrupt("return",e.accessToken);case 9:case"end":return t.stop()}var i}),t)})));return function(){return t.apply(this,arguments)}}(),isLoggedIn:n,setToken:c,subscribe:function(e){i.push(e)},unsubscribe:function(e){i=i.filter((function(t){return t!==e}))},getUserId:function(){return t()}}},p=function(){var e=O();return{useAuth:function(){var t=Object(r.useState)(e.isLoggedIn()),n=Object(o.a)(t,2),i=n[0],c=n[1];return Object(r.useEffect)((function(){var t=function(e){c(e)};return e.subscribe(t),function(){e.unsubscribe(t)}}),[]),[i]},authFetch:function(){var t=Object(h.a)(b.a.mark((function t(n,r){var i;return b.a.wrap((function(t){for(;;)switch(t.prev=t.next){case 0:return t.next=2,e.getToken();case 2:if(i=t.sent,r=r||{},""!==i){t.next=6;break}return t.abrupt("return",fetch(n,r));case 6:return r.headers=Object(u.a)(Object(u.a)({},r.headers),{},{Authorization:"".concat(i)}),t.abrupt("return",fetch(n,r));case 8:case"end":return t.stop()}}),t)})));return function(e,n){return t.apply(this,arguments)}}(),login:function(t){e.setToken(t)},logout:function(){e.setToken(null)},getUserId:function(){return e.getUserId()}}}(),f=p.useAuth,x=p.authFetch,m=p.login,g=p.logout,v=p.getUserId,y=(n(448),n(210)),w=(n(449),n(676)),C=n(408),S=n.n(C),k=n(3);var T=function(){var e,t=Object(r.useContext)(qe),n=Object(l.g)(),i=f(),c=Object(o.a)(i,1)[0],s=Object(r.useState)(!1),a=Object(o.a)(s,2),u=a[0],d=a[1],b=Object(r.useState)(""),h=Object(o.a)(b,2),O=h[0],p=h[1];return t.avatar.trigger=u,t.avatar.fetch=d,Object(r.useEffect)((function(){return console.log(c),console.log("fetch ava"),void(c&&x("".concat(window.location.origin,"/users/avatar/").concat(v())).then((function(e){if(e.ok)return e.json();if(403===e.status)return t.showAuth(!0),n.push("/"),{body:{url:""}};throw new Error("Error code: "+e.status)})).then((function(e){p(e.url)})).catch((function(e){console.log(e),n.push("/error")})))}),[u]),Object(k.jsxs)("div",{style:{display:"flex",flexDirection:"row",justifyContent:"space-between",flexGrow:0,flexShrink:1,flexBasis:"auto"},children:[Object(k.jsx)("div",{children:Object(k.jsx)("h1",{children:"Simple blog"})}),Object(k.jsxs)("div",{style:{display:"flex",alignItems:"center",marginRight:"3rem"},children:[Object(k.jsx)("div",{style:{marginRight:"1rem"}}),!0===c?Object(k.jsx)(y.a,(e={trigger:Object(k.jsx)(w.a,{src:O}),on:"hover",position:"bottom center",closeOnDocumentClick:!0,mouseEnterDelay:300},Object(j.a)(e,"mouseEnterDelay",0),Object(j.a)(e,"arrow",!1),Object(j.a)(e,"children",Object(k.jsxs)("div",{className:"user-popup-menu",children:[Object(k.jsx)("div",{className:"user-popup-menu-item",children:Object(k.jsx)("button",{className:"btn",onClick:function(){n.push("/post/user")},children:"My posts"})}),Object(k.jsx)("div",{className:"user-popup-menu-item",children:Object(k.jsx)("button",{className:"btn",onClick:function(){n.push("/profile")},children:"Profile"})}),Object(k.jsx)("div",{className:"user-popup-menu-item",children:"Statistics"}),Object(k.jsx)("div",{className:"user-popup-menu-item",style:{borderBottom:"none"},children:Object(k.jsx)("button",{className:"btn",onClick:function(){g(),n.push("/")},children:"Logout"})})]})),e)):Object(k.jsx)("div",{onClick:function(){t.showAuth(!0)},style:{cursor:"pointer"},children:Object(k.jsx)(S.a,{fontSize:"large"})})]})]})},E=n(94),R=n(128),N=n.n(R),D=n(129),P=n.n(D),A=n(219),L=n.n(A),I=n(220),U=n.n(I);var B=function(e){var t=e.post;return Object(k.jsxs)("div",{style:{display:"flex",flexDirection:"row",margin:"0.5rem",justifyContent:"space-around"},children:[Object(k.jsxs)("div",{style:{display:"flex",flexDirection:"row"},children:[Object(k.jsx)("div",{style:{marginRight:"0.25rem"},children:Object(k.jsx)(N.a,{})}),Object(k.jsx)("div",{children:t.postStatistics.likeCount})]}),Object(k.jsxs)("div",{style:{display:"flex",flexDirection:"row"},children:[Object(k.jsx)("div",{style:{marginRight:"0.25rem"},children:Object(k.jsx)(P.a,{})}),Object(k.jsx)("div",{children:t.postStatistics.dislikeCount})]}),Object(k.jsx)("div",{children:function(e){var t=new Date(e+"Z").toLocaleDateString("en-US").split("/"),n=Object(o.a)(t,3),r=n[0],i=n[1],c=n[2];return"".concat(i,".").concat(r,".").concat(c)}(t.publicationTime)}),Object(k.jsxs)("div",{style:{display:"flex",flexDirection:"row"},children:[Object(k.jsx)("div",{style:{marginRight:"0.25rem"},children:Object(k.jsx)(L.a,{})}),Object(k.jsx)("div",{children:t.postStatistics.viewCount})]}),Object(k.jsxs)("div",{style:{display:"flex",flexDirection:"row"},children:[Object(k.jsx)("div",{style:{marginRight:"0.25rem"},children:Object(k.jsx)(U.a,{})}),Object(k.jsx)("div",{children:t.postStatistics.commentCount})]})]})};var V=function(e){var t=e.post,n=Object(l.g)();return Object(k.jsxs)("div",{style:{width:375,border:"solid",borderWidth:1,borderRadius:15,borderColor:"rgb(228, 228, 228)",margin:"0.5rem"},children:[Object(k.jsx)("div",{children:Object(k.jsx)("button",{className:"btn",onClick:function(){n.push("/post/view/"+t.id)},children:Object(k.jsx)("div",{children:Object(k.jsx)("img",{src:t.postCardImageUrl,style:{borderRadius:10,width:"100%"}})})})}),Object(k.jsx)("div",{style:{marginLeft:"0.5rem",marginRight:"0.5rem"},children:Object(k.jsx)("span",{style:{fontWeight:600},children:t.title})}),Object(k.jsx)("div",{children:Object(k.jsx)(B,{post:t})})]})},J=n(221),M=n(348);var F=function(){var e=Object(l.g)(),t=Object(r.useContext)(qe),n=Object(r.useState)({postList:[],pageNumber:-1,totalPages:2,hasNext:!0}),i=Object(o.a)(n,2),c=i[0],s=i[1],a=function(){var n=c.pageNumber+1,r=!(n===c.totalPages),i=new URL(window.location.origin+"/posts"),o={pageNumber:n,pageSize:12,sort:"id"};Object.keys(o).forEach((function(e){return i.searchParams.append(e,o[e])})),x(i).then((function(n){if(n.ok)return n.json();if(403!==n.status)throw new Error("Error code: "+n.status);t.showAuth(!0),e.push("/")})).then((function(e){s({postList:[].concat(Object(E.a)(c.postList),Object(E.a)(e.postCartDataList)),pageNumber:n,hasNext:r,totalPages:e.totalPages})})).catch((function(t){console.log(t),e.push("/error")}))};return Object(r.useEffect)((function(){return a()}),[]),Object(k.jsx)("div",{id:"scrollableDiv",style:{height:"100vh",overflowY:"auto"},children:Object(k.jsx)(J.a,{dataLength:c.postList.length,next:a,hasMore:c.hasNext,loader:Object(k.jsx)("div",{style:{width:"100%",display:"flex",justifyContent:"center"},children:Object(k.jsx)(M.a,{})}),endMessage:Object(k.jsx)("div",{style:{width:"100%"},children:Object(k.jsx)("span",{children:"No moaaar!!"})}),scrollableTarget:"scrollableDiv",style:{display:"flex",flexWrap:"wrap",marginLeft:"auto",marginRight:"auto"},children:c.postList.map((function(e){return Object(k.jsx)(V,{post:e},e.id)}))})})};var z=n(276),_=n(349),H=n(350);var W=function(e){var t=e.comment,n=e.isAuth,i=Object(r.useContext)(qe),c=n?{}:{cursor:"not-allowed",pointerEvents:"none"},s=v(),a=Object(l.g)(),j=Object(r.useState)(t.text),u=Object(o.a)(j,2),d=u[0],b=u[1],h=Object(r.useState)({isShow:!1,text:"",error:!1}),O=Object(o.a)(h,2),p=O[0],f=O[1];return Object(k.jsxs)("div",{style:{display:"flex",flexDirection:"row",marginTop:"0.5rem"},children:[Object(k.jsx)(w.a,{src:t.user.avatarUrl}),Object(k.jsxs)("div",{style:{width:"100%"},children:[Object(k.jsx)("div",{style:{marginLeft:"0.25rem"},children:Object(k.jsx)("span",{style:{fontWeight:600},children:"".concat(t.user.firstName," ").concat(t.user.lastName)})}),!0===t.isEnabled?Object(k.jsxs)("div",{children:[Object(k.jsx)("div",{style:{marginLeft:"0.5rem"},children:d}),Object(k.jsx)("div",{style:{display:"flex",flexDirection:"row"},children:Object(k.jsxs)("div",{style:{display:"flex",flexDirection:"row"},children:[Object(k.jsxs)("div",{style:{display:"flex",flexDirection:"row",marginRight:"0.5rem"},children:[Object(k.jsx)("div",{style:{marginRight:"0.25rem"},children:Object(k.jsx)(z.a,{color:"primary",onClick:function(){},style:c,children:Object(k.jsx)(N.a,{})})}),Object(k.jsx)("div",{style:{paddingBottom:12,paddingTop:12},children:t.likes})]}),Object(k.jsxs)("div",{style:{display:"flex",flexDirection:"row",marginRight:"0.5rem",alignItems:"center"},children:[Object(k.jsx)("div",{style:{marginRight:"0.25rem"},children:Object(k.jsx)(z.a,{color:"primary",onClick:function(){},style:c,children:Object(k.jsx)(P.a,{})})}),Object(k.jsx)("div",{style:{paddingBottom:12,paddingTop:12},children:t.dislikes})]}),n&&t.user.id===s?Object(k.jsxs)("div",{style:{alignSelf:"center"},children:[Object(k.jsx)(_.a,{variant:"outlined",onClick:function(){f({isShow:!p.isShow,text:d,error:!1})},style:{marginRight:"1rem"},children:"Edit"}),p.isShow?Object(k.jsx)(_.a,{variant:"outlined",color:"primary",onClick:function(){""===p.text&&f({isShow:p.isShow,text:p.text,error:!0}),x("".concat(window.location.origin,"/comments/").concat(t.id),{method:"PUT",headers:{"Content-Type":"application/json;charset=utf-8"},body:JSON.stringify({postId:t.postId,text:p.text})}).then((function(e){if(e.ok)return e.json();if(403===e.status)return i.showAuth(!0),a.push("/"),{result:!1};throw new Error("Error code: "+e.status)})).then((function(e){!0===e.result&&(b(p.text),f({isShow:!1,text:"",error:!1}))})).catch((function(e){console.log(e),a.push("/error")}))},children:"Submit"}):Object(k.jsx)("div",{})]}):Object(k.jsx)("div",{})]})}),p.isShow?Object(k.jsx)("div",{children:Object(k.jsx)(H.a,{onChange:function(e){f({isShow:p.isShow,text:e.target.value,error:!1})},helperText:p.error?"Comment must not be empty":"",error:p.error,value:p.text,multiline:!0,rowsMax:5,variant:"outlined",style:{width:"100%"}})}):Object(k.jsx)("div",{})]}):Object(k.jsx)("div",{style:{marginTop:"1rem",marginBottom:"1rem"},children:Object(k.jsx)("span",{style:{color:"rgb(150,0,0)"},children:"The comment was blocked"})})]})]})},K=function(e){var t=new Date(e+"Z").toLocaleDateString("en-US").split("/"),n=Object(o.a)(t,3),r=n[0],i=n[1],c=n[2];return"".concat(i,".").concat(r,".").concat(c)};var G=function(e){var t=e.postId,n=e.comments,i=e.setComments,c=e.postData,s=e.setPostData,a=Object(r.useContext)(qe),j=Object(l.g)(),d=Object(r.useState)(""),b=Object(o.a)(d,2),h=b[0],O=b[1],p=Object(r.useState)(!1),f=Object(o.a)(p,2),m=f[0],g=f[1];return Object(k.jsxs)("div",{children:[Object(k.jsx)("div",{children:Object(k.jsx)(H.a,{helperText:m?"Comment can't be empty":"",error:m,onChange:function(e){g(!1),O(e.target.value)},multiline:!0,rowsMax:5,variant:"outlined",style:{width:"100%"}})}),Object(k.jsx)("div",{style:{marginTop:"1rem",marginBottom:"1rem",textAlign:"center"},children:Object(k.jsx)(_.a,{variant:"contained",color:"primary",onClick:function(){""!==h?x("".concat(window.location.origin,"/comments"),{method:"POST",headers:{"Content-Type":"application/json;charset=utf-8"},body:JSON.stringify({postId:t,text:h})}).then((function(e){if(e.ok)return e.json();if(403!==e.status)throw new Error("Error code: "+e.status);a.showAuth(!0),j.push("/"),i({commentList:[],pageNumber:0,totalPages:0,hasNext:!1})})).then((function(e){if(!1!==e.result){i({commentList:[e].concat(Object(E.a)(n.commentList)),pageNumber:n.nextPage,totalPages:n.totalPages,hasNext:n.hasNext});var t=Object(u.a)({},c.post);t.postStatistics.commentCount=t.postStatistics.commentCount+1,s({post:t,isVote:c.isVote,voteValue:c.voteValue})}})).catch((function(e){console.log(typeof e),console.log(e),j.push("/error")})):g(!0)},children:"Submit"})})]})};var Q=function(){var e=Object(r.useContext)(qe),t=Object(l.h)().id,n=f(),i=Object(o.a)(n,1)[0],c=Object(l.g)(),s=Object(r.useState)({post:{postStatistics:{},user:{firstName:"",lastName:""}}}),a=Object(o.a)(s,2),j=a[0],d=a[1],b=Object(r.useState)({commentList:[],pageNumber:-1,totalPages:2,hasNext:!0}),h=Object(o.a)(b,2),O=h[0],p=h[1],m=function(e){return i?!0===j.isVote?e?j.voteValue?{cursor:"not-allowed",pointerEvents:"none",color:"rgb(15,148,15)"}:{}:j.voteValue?{}:{cursor:"not-allowed",pointerEvents:"none",color:"rgb(15,148,15)"}:{}:{cursor:"not-allowed",pointerEvents:"none"}},g=function(){var n=O.pageNumber+1,r=!(n===O.totalPages),i=new URL("".concat(window.location.origin,"/comments")),s={pageNumber:n,pageSize:12,sort:"id",postId:t};Object.keys(s).forEach((function(e){return i.searchParams.append(e,s[e])})),x(i).then((function(t){if(t.ok)return t.json();if(403===t.status)return e.showAuth(!0),c.push("/"),{commentList:[],pageNumber:0,totalPages:0,hasNext:!1};throw new Error("Error code: "+t.status)})).then((function(e){p({commentList:[].concat(Object(E.a)(O.commentList),Object(E.a)(e.commentList)),pageNumber:n,totalPages:e.totalPages,hasNext:r})})).catch((function(e){console.log(e),c.push("/error")}))};return Object(r.useEffect)((function(){return function(){var n=new URL("".concat(window.location.origin,"/posts/").concat(t));x(n).then((function(t){if(t.ok)return t.json();if(403!==t.status)throw new Error("Error code: "+t.status);e.showAuth(!0),c.push("/"),d({})})).then((function(e){return d(e)})).catch((function(e){console.log(e),c.push("/error")}))}()}),[]),Object(r.useEffect)((function(){return g()}),[]),Object(k.jsx)("div",{children:Object(k.jsx)("div",{id:"comment-c",style:{height:"100vh",overflowY:"auto"},children:Object(k.jsxs)(J.a,{dataLength:O.commentList.length,next:g,hasMore:O.hasNext,loader:Object(k.jsx)(M.a,{}),endMessage:Object(k.jsx)("div",{style:{width:"100%"},children:Object(k.jsx)("span",{children:"No moaaar!!"})}),scrollableTarget:"comment-c",style:{margin:"1rem"},children:[Object(k.jsxs)("div",{style:{display:"flex",flexDirection:"row",alignItems:"center"},children:[Object(k.jsx)("div",{style:{marginRight:"0.25rem"},children:Object(k.jsx)(w.a,{src:j.post.avatarUrl})}),Object(k.jsx)("div",{style:{marginRight:"0.25rem"},children:"".concat(j.post.user.firstName," ").concat(j.post.user.lastName)}),Object(k.jsx)("div",{style:{marginRight:"0.25rem"},children:Object(k.jsx)("div",{children:K(j.post.publicationTime)})})]}),Object(k.jsx)("div",{children:Object(k.jsx)("h1",{children:j.post.title})}),Object(k.jsx)("div",{style:{marginBottom:"1rem"},dangerouslySetInnerHTML:{__html:j.post.text}}),Object(k.jsx)("div",{children:Object(k.jsxs)("div",{style:{display:"flex",flexDirection:"row",margin:"1rem"},children:[Object(k.jsxs)("div",{style:{display:"flex",flexDirection:"row",marginRight:"0.5rem"},children:[Object(k.jsx)("div",{style:{marginRight:"0.25rem"},children:Object(k.jsx)(z.a,{color:"primary",onClick:function(){x("".concat(window.location.origin,"/votes"),{method:"POST",headers:{"Content-Type":"application/json;charset=utf-8"},body:JSON.stringify({postId:j.post.id,value:!0})}).then((function(t){if(t.ok)return t.json();if(403===t.status)return e.showAuth(!0),c.push("/"),{result:!1};throw new Error("Error code: "+t.status)})).then((function(e){if(!0===e.result){var t=Object(u.a)({},j.post);t.postStatistics.likeCount=t.postStatistics.likeCount+1,t.postStatistics.dislikeCount=t.postStatistics.dislikeCount-1,d({post:t,isVote:!0,voteValue:!0})}})).catch((function(e){console.log(e),c.push("/error")}))},style:m(!0),children:Object(k.jsx)(N.a,{})})}),Object(k.jsx)("div",{style:{paddingBottom:12,paddingTop:12},children:j.post.postStatistics.likeCount})]}),Object(k.jsxs)("div",{style:{display:"flex",flexDirection:"row",marginRight:"0.5rem"},children:[Object(k.jsx)("div",{style:{marginRight:"0.25rem"},children:Object(k.jsx)(z.a,{color:"primary",onClick:function(){x("".concat(window.location.origin,"/votes"),{method:"POST",headers:{"Content-Type":"application/json;charset=utf-8"},body:JSON.stringify({postId:j.post.id,value:!1})}).then((function(t){if(t.ok)return t.json();if(403===t.status)return e.showAuth(!0),c.push("/"),{result:!1};throw new Error("Error code: "+t.status)})).then((function(e){if(!0===e.result){var t=Object(u.a)({},j.post);t.postStatistics.likeCount=t.postStatistics.likeCount-1,t.postStatistics.dislikeCount=t.postStatistics.dislikeCount+1,d({post:t,isVote:!0,voteValue:!1})}})).catch((function(e){console.log(e),c.push("/error")}))},style:m(!1),children:Object(k.jsx)(P.a,{})})}),Object(k.jsx)("div",{style:{paddingBottom:12,paddingTop:12},children:j.post.postStatistics.dislikeCount})]}),Object(k.jsxs)("div",{style:{display:"flex",flexDirection:"row",marginRight:"0.5rem"},children:[Object(k.jsx)("div",{style:{marginRight:"0.25rem",paddingBottom:12,paddingTop:12},children:Object(k.jsx)(L.a,{})}),Object(k.jsx)("div",{style:{paddingBottom:12,paddingTop:12},children:j.post.postStatistics.viewCount})]}),Object(k.jsxs)("div",{style:{display:"flex",flexDirection:"row",marginRight:"0.5rem"},children:[Object(k.jsx)("div",{style:{marginRight:"0.25rem",paddingBottom:12,paddingTop:12},children:Object(k.jsx)(U.a,{})}),Object(k.jsx)("div",{style:{paddingBottom:12,paddingTop:12},children:j.post.postStatistics.commentCount})]})]})}),Object(k.jsx)("hr",{}),Object(k.jsx)("div",{children:Object(k.jsx)("h2",{children:"Comments"})}),i?Object(k.jsx)("div",{children:Object(k.jsx)(G,{postId:j.post.id,comments:O,setComments:p,postData:j,setPostData:d})}):Object(k.jsx)("div",{children:Object(k.jsx)("h4",{children:"Comments allowed only for registered users"})}),O.commentList.length>0?O.commentList.map((function(e){return Object(k.jsx)(W,{comment:e,isAuth:i},e.id)})):Object(k.jsx)("div",{children:"There is no comments"})]})})})},Y=n(227),Z=n.n(Y),q=n(411),X=n.n(q),$=n(420),ee=n.n($),te=n(412),ne=n.n(te),re=n(418),ie=n.n(re),ce=n(286),se=n.n(ce),oe=n(284),ae=n.n(oe),le=n(285),je=n.n(le),ue=n(413),de=n.n(ue),be=n(415),he=n.n(be),Oe=n(416),pe=n.n(Oe),fe=n(417),xe=n.n(fe),me=n(421),ge=n.n(me),ve=n(414),ye=n.n(ve),we=n(419),Ce=n.n(we),Se=n(422),ke=n.n(Se),Te=n(423),Ee=n.n(Te),Re=n(424),Ne=n.n(Re),De=n(403),Pe=n(373),Ae=n(365),Le=n(675),Ie={Add:Object(r.forwardRef)((function(e,t){return Object(k.jsx)(X.a,Object(u.a)(Object(u.a)({},e),{},{ref:t}))})),Check:Object(r.forwardRef)((function(e,t){return Object(k.jsx)(ne.a,Object(u.a)(Object(u.a)({},e),{},{ref:t}))})),Clear:Object(r.forwardRef)((function(e,t){return Object(k.jsx)(ae.a,Object(u.a)(Object(u.a)({},e),{},{ref:t}))})),Delete:Object(r.forwardRef)((function(e,t){return Object(k.jsx)(je.a,Object(u.a)(Object(u.a)({},e),{},{ref:t}))})),DetailPanel:Object(r.forwardRef)((function(e,t){return Object(k.jsx)(se.a,Object(u.a)(Object(u.a)({},e),{},{ref:t}))})),Edit:Object(r.forwardRef)((function(e,t){return Object(k.jsx)(de.a,Object(u.a)(Object(u.a)({},e),{},{ref:t}))})),Export:Object(r.forwardRef)((function(e,t){return Object(k.jsx)(ye.a,Object(u.a)(Object(u.a)({},e),{},{ref:t}))})),Filter:Object(r.forwardRef)((function(e,t){return Object(k.jsx)(he.a,Object(u.a)(Object(u.a)({},e),{},{ref:t}))})),FirstPage:Object(r.forwardRef)((function(e,t){return Object(k.jsx)(pe.a,Object(u.a)(Object(u.a)({},e),{},{ref:t}))})),LastPage:Object(r.forwardRef)((function(e,t){return Object(k.jsx)(xe.a,Object(u.a)(Object(u.a)({},e),{},{ref:t}))})),NextPage:Object(r.forwardRef)((function(e,t){return Object(k.jsx)(se.a,Object(u.a)(Object(u.a)({},e),{},{ref:t}))})),PreviousPage:Object(r.forwardRef)((function(e,t){return Object(k.jsx)(ie.a,Object(u.a)(Object(u.a)({},e),{},{ref:t}))})),ResetSearch:Object(r.forwardRef)((function(e,t){return Object(k.jsx)(ae.a,Object(u.a)(Object(u.a)({},e),{},{ref:t}))})),Search:Object(r.forwardRef)((function(e,t){return Object(k.jsx)(Ce.a,Object(u.a)(Object(u.a)({},e),{},{ref:t}))})),SortArrow:Object(r.forwardRef)((function(e,t){return Object(k.jsx)(ee.a,Object(u.a)(Object(u.a)({},e),{},{ref:t}))})),ThirdStateCheck:Object(r.forwardRef)((function(e,t){return Object(k.jsx)(ge.a,Object(u.a)(Object(u.a)({},e),{},{ref:t}))})),ViewColumn:Object(r.forwardRef)((function(e,t){return Object(k.jsx)(ke.a,Object(u.a)(Object(u.a)({},e),{},{ref:t}))}))};var Ue,Be=function(){var e=Object(l.g)(),t=Object(r.useRef)(),n=Object(r.useContext)(qe),i="MODERATED",c="PUBLISHED",s="DECLINED",a="INACTIVE",j="FUTURE",d="ALL",b=Object(r.useState)(c),h=Object(o.a)(b,2),O=h[0],p=h[1],f=Object(r.useState)({isOpen:!1,id:null}),m=Object(o.a)(f,2),g=m[0],v=m[1],w=function(e){p(e.target.value),t.current.onQueryChange()},C=function(){e.push("/post/new")};return Object(k.jsxs)("div",{children:[Object(k.jsx)(y.a,{open:g.isOpen,onClose:function(){v(!1)},children:Object(k.jsx)("div",{className:"modal",children:Object(k.jsx)("div",{className:"modal-main",style:{width:300},children:Object(k.jsxs)("div",{style:{margin:"2rem"},children:[Object(k.jsx)("div",{style:{marginBottom:"2rem"},children:Object(k.jsx)("h2",{style:{textAlign:"center"},children:"The post will be permanently deleted!"})}),Object(k.jsxs)("div",{style:{display:"flex",justifyContent:"space-around"},children:[Object(k.jsx)(_.a,{variant:"contained",color:"primary",onClick:function(){return function(){var n=g.id;v({isOpen:!1,id:null}),x("".concat(window.location.origin,"/posts/").concat(n),{method:"DELETE"}).then((function(e){if(e.ok)return e.json();if(403===e.status)return{result:!1};throw new Error("Error code: "+e.status)})).then((function(e){!0===e.result&&t.current.onQueryChange()})).catch((function(t){console.log(t),e.push("/error")}))}()},children:"Confirm"}),Object(k.jsx)(_.a,{variant:"contained",color:"default",onClick:function(){return v({isOpen:!1,id:null})},children:"Cancel"})]})]})})})}),Object(k.jsx)(Z.a,{tableRef:t,style:{boxShadow:"none"},columns:[{title:"Header",field:"postCardImageUrl",render:function(e){return Object(k.jsx)("img",{style:{width:"100%",borderRadius:15},src:e.postCardImageUrl})},width:null,cellStyle:{width:"10%"},editable:"never"},{title:"Active",field:"isActive",render:function(e){return Object(k.jsx)(De.a,{color:"primary",checked:e.isActive})},width:null,cellStyle:{width:"5%"},editComponent:function(e){return Object(k.jsx)("div",{children:Object(k.jsx)(De.a,{color:"primary",checked:e.rowData.isActive,onChange:function(t){return e.onChange(t.target.checked)}})})}},{title:"Publication time",field:"publicationTime",width:null,cellStyle:{width:"25%"},render:function(e){return K(e.publicationTime)},editComponent:function(e){return Object(k.jsx)("div",{children:Object(k.jsx)(H.a,{type:"datetime-local",value:e.rowData.publicationTime,onChange:function(t){return e.onChange(t.target.value)}})})}},{title:"Title",field:"title",width:null,cellStyle:{width:"60%"},editable:"never"}],components:{Toolbar:function(e){return Object(k.jsxs)("div",{children:[Object(k.jsx)(Y.MTableToolbar,Object(u.a)({},e)),Object(k.jsxs)("div",{style:{display:"flex",flexDirection:"row",alignItems:"center",justifyContent:"space-between"},children:[Object(k.jsxs)(Pe.a,{style:{flexDirection:"row"},"aria-label":"Post status",value:O,onChange:w,children:[Object(k.jsx)(Ae.a,{labelPlacement:"top",value:c,control:Object(k.jsx)(Le.a,{color:"primary"}),label:"Published"}),Object(k.jsx)(Ae.a,{labelPlacement:"top",value:i,control:Object(k.jsx)(Le.a,{color:"primary"}),label:"Moderated"}),Object(k.jsx)(Ae.a,{labelPlacement:"top",value:s,control:Object(k.jsx)(Le.a,{color:"primary"}),label:"Declined"}),Object(k.jsx)(Ae.a,{labelPlacement:"top",value:a,control:Object(k.jsx)(Le.a,{color:"primary"}),label:"Inactive"}),Object(k.jsx)(Ae.a,{labelPlacement:"top",value:j,control:Object(k.jsx)(Le.a,{color:"primary"}),label:"Future"}),Object(k.jsx)(Ae.a,{labelPlacement:"top",value:d,control:Object(k.jsx)(Le.a,{color:"primary"}),label:"All"})]}),Object(k.jsx)("div",{children:Object(k.jsx)(_.a,{color:"primary",variant:"contained",onClick:C,children:"New post"})})]})]})}},data:function(t){return new Promise((function(r,i){var c=new URL(window.location.origin+"/posts/user"),s={pageNumber:t.page,pageSize:t.pageSize,status:O,sort:"id"};Object.keys(s).forEach((function(e){return c.searchParams.append(e,s[e])})),x(c).then((function(t){if(t.ok)return t.json();if(403===t.status)return n.showAuth(!0),e.push("/"),{postByUserList:[],page:0,totalElements:0};throw new Error("Error code: "+t.status)})).then((function(e){r({data:e.postByUserList,page:e.page,totalCount:e.totalElements})})).catch((function(t){console.log(t),e.push("/error")}))}))},editable:{onRowUpdate:function(t,r){return new Promise((function(r,i){var c="".concat(window.location.origin,"/posts/").concat(t.id);x(c,{method:"PUT",headers:{"Content-Type":"application/json;charset=utf-8"},body:JSON.stringify({title:null,text:null,publicationTime:t.publicationTime,isActive:t.isActive,postCardImageUrl:null})}).then((function(t){if(t.ok)r();else{if(403!==t.status)throw new Error("Error code: "+t.status);n.showAuth(),e.push("/")}}))})).catch((function(t){console.log(t),e.push("/error")}))}},actions:[{icon:function(){return Object(k.jsx)(je.a,{color:"primary"})},tooltip:"Delete post",onClick:function(e,t){return v({isOpen:!0,id:t.id})}},{icon:function(){return Object(k.jsx)(Ee.a,{})},tooltip:"Edit page",onClick:function(t,n){return r=n.id,void e.push("/post/edit/"+r);var r}},{icon:function(){return Object(k.jsx)(Ne.a,{})},tooltip:"View",onClick:function(t,n){return r=n.id,void e.push("/post/view/"+r);var r}}],icons:Ie,title:"All posts list",options:{actionsColumnIndex:5}})]})},Ve=n(425),Je=n.n(Ve),Me=(n(617),n(426)),Fe=n.n(Me),ze=function(){var e=Object(h.a)(b.a.mark((function e(t){var n;return b.a.wrap((function(e){for(;;)switch(e.prev=e.next){case 0:n=new URL(window.location.origin+"/images/upload"),x(n,{method:"POST",body:t}).then((function(e){e.ok?e.json().then((function(e){var t=e.url,n=Ue.getEditorSelection();Ue.getEditor().insertEmbed(n.index,"image",t)})):console.log("!!!")}));case 2:case"end":return e.stop()}}),e)})));return function(t){return e.apply(this,arguments)}}(),_e={toolbar:{container:[[{header:"1"},{header:"2"},{font:[]}],[{size:[]}],["bold","italic","underline","strike","blockquote"],[{list:"ordered"},{list:"bullet"},{indent:"-1"},{indent:"+1"},{align:[]}],["link","image","video"],["clean"]],handlers:{image:function(){var e=document.createElement("input");e.setAttribute("type","file"),e.setAttribute("accept","image/*"),e.click(),e.onchange=Object(h.a)(b.a.mark((function t(){var n,r;return b.a.wrap((function(t){for(;;)switch(t.prev=t.next){case 0:return n=e.files[0],(r=new FormData).append("image",n),t.next=5,ze(r);case 5:t.sent;case 6:case"end":return t.stop()}}),t)})))}}},clipboard:{matchVisual:!1}};var He=function(e){var t=e.postId,n=e.isEdit,i=e.isNew,c=Object(r.useContext)(qe),s=Object(l.h)().pathId,a=void 0===t?s:t,j={title:"",isActive:!1,publicationTime:new Date,postCardImageUrl:"",text:""},d=Object(l.g)(),b=Object(r.useState)(j.text),h=Object(o.a)(b,2),O=h[0],p=h[1],f=Object(r.useState)({title:j.title,isActive:j.isActive}),m=Object(o.a)(f,2),g=m[0],v=m[1],y=Object(r.useState)(j.publicationTime),w=Object(o.a)(y,2),C=w[0],S=w[1],T=Object(r.useState)(j.postCardImageUrl),E=Object(o.a)(T,2),R=E[0],N=E[1];return Object(r.useEffect)((function(){return n&&x("".concat(window.location.origin,"/posts/user/").concat(a)).then((function(e){if(e.ok)return e.json();if(403!==e.status)throw new Error("Error code: "+e.status);c.showAuth(!0),d.push("/")})).then((function(e){p(e.text),N(e.postCardImageUrl),v({title:e.title,isActive:e.isActive}),S(e.publicationTime)})).catch((function(e){console.log(e),d.push("/error")})),void(i&&(p(j.text),N(j.postCardImageUrl),v({title:j.title,isActive:j.isActive}),S(j.publicationTime)))}),[a]),Object(k.jsxs)("div",{children:[Object(k.jsxs)("div",{children:[Object(k.jsxs)("div",{children:[Object(k.jsx)("div",{style:{textAlign:"center"},children:n?Object(k.jsx)("h3",{children:"Update post"}):Object(k.jsx)("h3",{children:"Create new post"})}),Object(k.jsx)("hr",{})]}),Object(k.jsxs)("div",{style:{display:"flex",flexDirection:"row"},children:[Object(k.jsxs)("div",{style:{width:"50%",margin:"0.5rem",display:"flex",flexDirection:"column",justifyContent:"space-between"},children:[Object(k.jsxs)("div",{style:{margin:"0.5rem"},children:[Object(k.jsx)("div",{children:"Title"}),Object(k.jsx)("textarea",{name:"title",style:{width:"100%",resize:"none"},rows:"5",value:g.title,onChange:function(e){v(Object(u.a)(Object(u.a)({},g),{},{title:e.target.value}))}})]}),Object(k.jsxs)("div",{style:{margin:"0.5rem"},children:[Object(k.jsx)("div",{children:"Publication time"}),Object(k.jsx)(Fe.a,{onChange:S,value:new Date(C)})]}),Object(k.jsxs)("div",{style:{margin:"0.5rem"},children:[Object(k.jsx)("input",{type:"checkbox",name:"isActive",checked:g.isActive,onChange:function(e){v(Object(u.a)(Object(u.a)({},g),{},{isActive:e.target.checked}))}}),Object(k.jsx)("span",{children:"Is this post will be active?"})]})]}),Object(k.jsxs)("div",{style:{display:"flex",flexDirection:"column",justifyContent:"space-between",width:"50%",margin:"0.5rem"},children:[Object(k.jsxs)("div",{style:{margin:"0.5rem"},children:["Header image",Object(k.jsx)("span",{children:"(will be centered and clipped to 5x3)"})]}),Object(k.jsxs)("div",{children:[Object(k.jsx)("img",{src:R,style:{width:"100%",borderRadius:15}}),Object(k.jsx)("div",{style:{margin:"0.5rem"},children:Object(k.jsx)("input",{type:"file",accept:"/image*",onChange:function(e){var t=new FormData;t.append("xScale",5),t.append("yScale",3),t.append("image",e.target.files[0]);var n=new URL(window.location.origin+"/images/header/upload");x(n,{method:"POST",body:t}).then((function(e){if(e.ok)return e.json();if(403===e.status)return c.showAuth(!0),d.push("/"),{url:""};throw new Error("Error code: "+e.status)})).then((function(e){var t=e.url;N(t)})).catch((function(e){console.log(e),d.push("/error")}))}})})]})]})]}),Object(k.jsx)(Je.a,{ref:function(e){return Ue=e},value:O,modules:_e,onChange:function(e){p(e)}})]}),Object(k.jsx)("div",{style:{margin:"1rem",display:"flex",justifyContent:"center"},children:Object(k.jsx)("div",{children:Object(k.jsx)(_.a,{variant:"contained",color:"primary",onClick:function(){var e=n?"".concat(window.location.origin,"/posts/").concat(a):"".concat(window.location.origin,"/posts");x(e,{method:n?"PUT":"POST",headers:{"Content-Type":"application/json;charset=utf-8"},body:JSON.stringify({title:g.title,text:O,publicationTime:C,isActive:g.isActive,postCardImageUrl:R})}).then((function(e){if(e.ok)return e.json();if(403===e.status)return c.showAuth(!0),d.push("/"),{result:!1};throw new Error("Error code: "+e.status)})).then((function(e){!0===e.result&&d.push("/post/user")})).catch((function(e){console.log(e),d.push("/error")}))},children:"Save"})})})]})};var We=function(){return Object(k.jsx)("h1",{children:"Page not found"})},Ke=n(427),Ge=n.n(Ke);n(639);var Qe=function(e){var t=e.show,n=e.handleClose,i=Object(r.useContext)(qe),c=!0===t?"modal display-block":"modal display-none",s=Object(r.useState)({username:"",password:""}),a=Object(o.a)(s,2),l=a[0],d=a[1],b=Object(r.useState)(!1),h=Object(o.a)(b,2),O=h[0],p=h[1],f=function(e){var t=e.target,n=t.name,r=t.value;d(Object(u.a)(Object(u.a)({},l),{},Object(j.a)({},n,r))),p(!1)};return Object(k.jsx)("div",{className:c,children:Object(k.jsxs)("div",{className:"modal-main",children:[Object(k.jsx)("div",{style:{display:"flex",justifyContent:"flex-end",position:"absolute",right:0,top:0},children:Object(k.jsx)(z.a,{onClick:n,children:Object(k.jsx)(Ge.a,{})})}),Object(k.jsx)("form",{onSubmit:function(e){e&&e.preventDefault(),fetch("/login",{method:"POST",body:JSON.stringify(l)}).then((function(e){if(e.ok){var t=e.headers.get("Authorization"),n=e.headers.get("Refresh");if(null===t||null===n)throw new Error;return{accessToken:t,refreshToken:n}}throw new Error})).then((function(e){m(e),d({username:"",password:""}),i.showAuth(!1),i.avatar.fetch(!i.avatar.trigger),console.log(i)})).catch((function(e){return p(!0)}))},children:Object(k.jsxs)("div",{style:{display:"flex",flexDirection:"column",alignItems:"center",margin:"1rem"},children:[Object(k.jsx)("h2",{style:{textAlign:"center"},children:"Please sign in"}),Object(k.jsx)("div",{style:{marginBottom:"0.5rem"},children:Object(k.jsx)(H.a,{error:O,variant:"outlined",name:"username",value:l.username,label:"Email",onChange:f})}),Object(k.jsx)("div",{style:{marginBottom:"0.5rem"},children:Object(k.jsx)(H.a,{error:O,helperText:O?"incorrect email or password":"",variant:"outlined",type:"password",name:"password",label:"Password",value:l.password,onChange:f})}),Object(k.jsx)("div",{style:{marginBottom:"0.5rem"},children:Object(k.jsx)(_.a,{type:"submit",variant:"contained",children:"Sign in"})})]})})]})})},Ye=n(430);var Ze=function(){Object(r.useContext)(qe);var e=Object(l.g)(),t=f(),n=Object(o.a)(t,1)[0],i=v(),c=Object(r.useState)({role:{name:""}}),s=Object(o.a)(c,2),a=s[0],j=s[1],d=Object(r.useState)({readOnly:!0}),b=Object(o.a)(d,2),h=b[0],O=b[1],p=Object(r.useState)(!1),m=Object(o.a)(p,2),g=m[0],y=m[1],w=Object(r.useCallback)((function(e){var t=URL.createObjectURL(e[0]);j(Object(u.a)(Object(u.a)({},a),{},{avatarUrl:t}))}),[]),C=Object(Ye.a)({onDrop:w,accept:["image/jpeg","image/png"],minSize:0,multiple:!1}),S=C.getRootProps,T=C.getInputProps,E=C.isDragActive,R=C.isDragReject;return Object(r.useEffect)((function(){n&&x("".concat(window.location.origin,"/users/").concat(i)).then((function(e){if(e.ok)return e.json();if(403===e.status)return{result:{}};throw new Error("Error code: "+e.status)})).then((function(e){console.log(e),j(e)})).catch((function(t){console.log(t),e.push("/error")}))}),[]),Object(k.jsxs)("div",{children:[Object(k.jsxs)("div",{style:{display:"flex",flexDirection:"row",justifyContent:"center"},children:[Object(k.jsx)("div",{style:{display:"flex",flexDirection:"column",width:200,justifyContent:"center",marginRight:"2rem"},children:Object(k.jsxs)("div",{children:[Object(k.jsx)("div",{children:Object(k.jsx)("img",{style:{width:"100%",border:"solid",borderWidth:1,borderRadius:15,color:"rgb(200,200,200)"},src:a.avatarUrl})}),g?Object(k.jsxs)("div",Object(u.a)(Object(u.a)({},S()),{},{style:{background:"rgb(230,230,230)",border:"dotted",color:"rgb(170,170,170)"},children:[Object(k.jsx)("input",Object(u.a)({},T())),Object(k.jsx)("div",{style:{height:60,display:"flex",alignItems:"center",justifyContent:"center"},children:R?Object(k.jsx)("div",{style:{color:"red",margin:"0.5rem"},children:"Files not supported"}):E?Object(k.jsx)("div",{style:{color:"black",margin:"0.5rem"},children:"Drop the files here ..."}):Object(k.jsx)("div",{style:{color:"black",margin:"0.5rem"},children:"Drag 'n' drop image here, or click to select"})})]})):Object(k.jsx)("div",{})]})}),Object(k.jsxs)("div",{children:[Object(k.jsx)("div",{style:{margin:"1rem"},children:Object(k.jsx)(H.a,{value:a.firstName,inputProps:h,style:{width:"100%"},helperText:"First name"})}),Object(k.jsx)("div",{style:{margin:"1rem"},children:Object(k.jsx)(H.a,{value:a.lastName,defaultValue:a.lastName,inputProps:h,style:{width:"100%"},helperText:"Last name"})}),Object(k.jsx)("div",{style:{margin:"1rem"},children:Object(k.jsx)(H.a,{value:a.email,helperText:"Email",inputProps:h,style:{width:"100%"}})}),Object(k.jsx)("div",{style:{margin:"1rem"},children:Object(k.jsx)(H.a,{value:a.registrationDate,type:"datetime-local",helperText:"Registration date",inputProps:h})}),Object(k.jsx)("div",{style:{margin:"1rem"},children:Object(k.jsx)(H.a,{value:a.role.name,helperText:"Permission",inputProps:h,style:{width:"100%"}})})]})]}),Object(k.jsxs)("div",{style:{display:"flex",justifyContent:"center"},children:[Object(k.jsx)("div",{style:{marginRight:"2rem"},children:Object(k.jsx)(_.a,{variant:"outlined",onClick:function(){y(!g),O({readOnly:!h.readOnly})},children:"edit"})}),g?Object(k.jsx)("div",{children:Object(k.jsx)(_.a,{variant:"outlined",color:"primary",children:"save"})}):Object(k.jsx)("div",{})]})]})},qe=i.a.createContext();var Xe=function(){var e=f(),t=Object(o.a)(e,1)[0],n=Object(r.useState)(!1),i=Object(o.a)(n,2),c=i[0],s=i[1];return Object(k.jsx)(qe.Provider,{value:{showAuth:s,avatar:{trigger:!1,fetch:null}},children:Object(k.jsxs)(a.a,{children:[Object(k.jsx)(Qe,{show:c,handleClose:s}),Object(k.jsx)(T,{}),Object(k.jsxs)("div",{style:{display:"flex",flexDirection:"row",flexGrow:1,flexShrink:1,flexBasis:"auto"},children:[Object(k.jsx)("div",{style:{}}),Object(k.jsx)("div",{style:{width:"50%"},children:Object(k.jsxs)(l.d,{children:[Object(k.jsx)(l.b,{exact:!0,path:"/",children:Object(k.jsx)(F,{})}),Object(k.jsx)(l.b,{path:"/post/view/:id",children:Object(k.jsx)(Q,{})}),Object(k.jsx)(l.b,{path:"/post/edit/:pathId",children:function(){return t?Object(k.jsx)(He,{isEdit:!0}):(s(!0),Object(k.jsx)(l.a,{to:"/"}))}}),Object(k.jsx)(l.b,{path:"/post/new",children:function(){return t?Object(k.jsx)(He,{isNew:!0}):(s(!0),Object(k.jsx)(l.a,{to:"/"}))}}),Object(k.jsx)(l.b,{path:"/post/user",children:function(){return t?Object(k.jsx)(Be,{}):(s(!0),Object(k.jsx)(l.a,{to:"/"}))}}),Object(k.jsx)(l.b,{path:"/profile",children:function(){return t?Object(k.jsx)(Ze,{}):(s(!0),Object(k.jsx)(l.a,{to:"/"}))}}),Object(k.jsx)(l.b,{path:"/error",children:Object(k.jsx)("h1",{children:"Error"})}),Object(k.jsx)(We,{})]})}),Object(k.jsx)("div",{})]})]})})};s.a.render(Object(k.jsx)(i.a.StrictMode,{children:Object(k.jsx)(Xe,{})}),document.getElementById("root"))}},[[641,1,2]]]);
//# sourceMappingURL=main.cd0ff2a1.chunk.js.map