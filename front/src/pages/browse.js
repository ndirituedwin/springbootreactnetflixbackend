import { BrowseContainer } from "../containers/browse";
import { useContent,useContentFilms } from "../hooks";
import selectionFilter from "../utils/selection-fiter";
export default function Browse({isAuthenticated,currentUser,currentuseravatar}){
    console.log("is the user authenticated from Browse page ?",isAuthenticated)
    console.log("is there a user currentuser from the browse page ?",currentUser)
    console.log("is there a user avatar from the browse page ?",currentuseravatar)
      
    // console.log("series valued",Object.values(useContent('series')))
      
    const {series}=useContent('series')
    const {films}=useContentFilms('films')
        console.log("series",series);
        console.log("films",films);  
         const slides=selectionFilter({series,films})
       
        console.log("logging slides",slides);
 
    return(
        <BrowseContainer slides={slides} isAuthenticated={isAuthenticated} currentUser={currentUser} currentuseravatar={currentuseravatar}/>
    )
}