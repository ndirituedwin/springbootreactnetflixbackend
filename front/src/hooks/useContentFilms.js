
import react,{useState,useEffect,useContext} from 'react';
import { fetchallfilms } from './../constants/ApiUtils';
export default function useContentFilms(target){
    const [content, setcontent] = useState()
    useEffect(() => {    
          fetchallfilms().then((response)=>{
            setcontent(response?.films)
          }).catch((error)=>{
              console.log("an exception has occurred while trying to fetch films from server",error.message);
          });
    }, [])
    return {[target]:content}

}