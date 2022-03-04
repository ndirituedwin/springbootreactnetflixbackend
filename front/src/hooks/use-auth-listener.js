

import { useEffect,useState } from 'react';
import { getCurrentUser } from '../constants/ApiUtils';
import { AUTHUSER } from '../constants/routes';
export default function useAuthListener(){
  
    const [user, setUser] = useState(
        JSON.parse(localStorage.getItem(AUTHUSER))
    )
    useEffect(() => {
     getCurrentUser().then((response)=>{
         console.log("response",response);
            if(response){
                localStorage.setItem(AUTHUSER,JSON.stringify(response))
                setUser(response)
            }else{
                localStorage.removeItem(AUTHUSER)
                setUser(null);
            }

        }).catch((error)=>{
            console.log("error inside useAuthistener",error.message)
        })
        // return ()=>listener();
    }, []);
    return {user};
}