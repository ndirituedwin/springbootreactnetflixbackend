
import react,{useState,useEffect,useContext} from 'react';
import { fetchallseries } from './../constants/ApiUtils';
export default function useContent(target){
    const [content, setcontent] = useState()
    
    useEffect(() => {
          fetchallseries().then((response)=>{
            setcontent(response?.series)
            
          }).catch((error)=>{
            console.log("an error occurred while trying to fetch series",error);
          });
          
        }, [])
        // console.log("response for series",content?.series)
    return {[target]:content}

}
