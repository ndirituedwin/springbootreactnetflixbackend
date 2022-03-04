import {BrowserRouter as Router,Route,Routes,Navigate} from 'react-router-dom'
import * as ROUTES from './constants/routes'
import Home from './pages/Home'
import {Browse, Signin, Signup} from './pages';
import react, { useEffect,useState } from 'react';
import { getCurrentUser, getcurrentuseravatar } from './constants/ApiUtils';
import LoadingIndicator from './constants/LoadingIndicator';
import useAuthListener from './hooks/use-auth-listener';
import axios from 'axios'
export default function App(props) {
      const user={useAuthListener}
      console.log("user from authstateListener",user);   
     const [currentUser, setcurrentUser] = useState(null)
     const [currentuseravatar, setcurrentuseravatar] = useState(null)
    const [isAuthenticated, setisAuthenticated] = useState(false)
    const [isLoading, setisLoading] = useState(true)
     const loadCurrentUser=()=>{
        getCurrentUser().then((response)=>{
          //    console.log("login current user",response)
              setcurrentUser(response),
              setisAuthenticated(true),
              setisLoading(false)
             
        }).catch((error)=>{
             console.log("error trying to load current user",error.message);
             setisLoading(false);
        })
     //    axios.get(ROUTES.GETCURRENTUSERAVATAR).then((response)=>{
        getcurrentuseravatar().then((response)=>{
             console.log("logging the current user avatar response ",response);
             setcurrentuseravatar(response)
          //    setDownloadUri(response.data.fileDownloadUri);
             setisLoading(false);
        }).catch((error)=>{
             console.log("error trying to fetch useravatar ",error)
             setisLoading(false)
        })
        
     }
     useEffect(() => {
          loadCurrentUser(); 
     }, [isAuthenticated]);

     function handeLogin(){
          console.log("logged in successfully")
          loadCurrentUser()


     }
     if(isLoading){
          <LoadingIndicator/> 
     }
   return(
     <Router>
          <Routes>
          {/* <Route exact path={ROUTES.HOME} element={<><JumbotronContainer /> <FaqsConntainer /> <FooterContainer/></>}/> */}
          <Route exact path={ROUTES.HOME} element={<Home/>}/>
          {isAuthenticated && (
               <Route exact path={ROUTES.BROWSE}  element={<Browse isAuthenticated={isAuthenticated} currentUser={currentUser} currentuseravatar={currentuseravatar}/>}/>
          )}
      
          {/* <Route element={ <ProtectedRoute isAuthenticated={isAuthenticated} path={ROUTES.BROWSE} exact>
               <Browse/>
          </ProtectedRoute>}/> */}
          {!isAuthenticated && (
                    <>
                    <Route exact path={ROUTES.SIGN_IN} element={<Signin currentUser={currentUser}  isAuthenticated={isAuthenticated} onLogin={handeLogin} {...props}/>}/>
                     <Route exact path={ROUTES.SIGN_UP} element={<Signup  isAuthenticated={isAuthenticated} />}/>
                   </>)
          }
          <Route path='*' element={<Navigate to={isAuthenticated? ROUTES.BROWSE:ROUTES.SIGN_IN}/>}/>
          
          </Routes>
       
     </Router>
     )
}
