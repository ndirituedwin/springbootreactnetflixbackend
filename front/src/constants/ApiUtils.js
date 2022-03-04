
import { ACCESS_TOKEN, APISIGNINURL,APISIGNUPURL, GETCURRENTUSER, GETUSERPROFILE,FETCHSERIES,FETCHFILMS, GETCURRENTUSERAVATAR } from './routes';
const request=(options)=>{
    const headers=new Headers({
        'Content-type':'application/json'
    });
    if(localStorage.getItem(ACCESS_TOKEN)){
        headers.append('Authorization','Bearer '+localStorage.getItem(ACCESS_TOKEN))
    }
    const defaults={headers:headers};
    options=Object.assign({},defaults,options);
    return fetch(options.url, options)
    .then(response => 
        response.json().then(json => {
            if(!response.ok) {
                return Promise.reject(json);
            }
            return json;
        })
    );
}
export function login(loginRequest){
    return request({
        url:APISIGNINURL,
        method:'POST',
        body:JSON.stringify(loginRequest)
    })
}

export function signup(signupRequest){
    return request({
        url:APISIGNUPURL,
        method:'POST',
        body:JSON.stringify(signupRequest)
    })
}
export function getCurrentUser(){
    if(!localStorage.getItem(ACCESS_TOKEN)){
        return Promise.reject("No access token set");
    }
    return request({
        url:GETCURRENTUSER,
        method:'GET'

    });
}
export function getcurrentuseravatar(){
    if(!localStorage.getItem(ACCESS_TOKEN)){
        return Promise.reject("No access token set");
    }
    return request({
        url:GETCURRENTUSERAVATAR,
        method:'GET'

    });
}
export function getUserProfile(username){
    return request({
        url: GETUSERPROFILE,
        method: 'GET'
    });
}
export function fetchallseries(){
    return request({
        url: FETCHSERIES,
        method: 'GET'
    });
}
export function fetchallfilms(){
    return request({
        url: FETCHFILMS,
        method: 'GET'
    });
}

