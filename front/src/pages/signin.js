
import react,{ useState,useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { HeaderContainer } from './../containers/header';
import { FooterContainer } from './../containers/Footer';
import { Form } from '../components';
import * as ROUTES from '../constants/routes'
import { ACCESS_TOKEN,TOKEN_TYPE } from '../constants/routes';
import axios from 'axios';
import { login } from './../constants/ApiUtils';

export default function Signin({currentUser,isAuthenticated,onLogin}){
  console.log("is the user authenticated ?",isAuthenticated);
  console.log("is the user in ?",currentUser);
    const navigate=useNavigate()
    const [emailAddress, setEmailAddress] = useState('')
    const [password, setPassword] = useState('')
    const [error, setError] = useState('')

    const isInvalid = password === '' || emailAddress === '';
    useEffect(() => {
       if(!currentUser){
       }else{
         navigate(ROUTES.BROWSE);
       }
    }, [])
   const handeSignin=(event)=>{
       event.preventDefault();
    
       const signindata={
           usernameorEmail:emailAddress,
           password:password
       }
       
       console.log("signin data",signindata);
         login(signindata)
           .then((response)=>{
               console.log("response after signin",response);
               localStorage.setItem(ACCESS_TOKEN,response.accessToken)
               localStorage.setItem(ROUTES.USERNAMEOREMAIL,response.usernameorEmail)
               localStorage.setItem(ROUTES.EXPIRESAT,response.expiresAt)
               localStorage.setItem(TOKEN_TYPE,response.tokenType)
               
               onLogin()
               navigate(ROUTES.BROWSE)

           }).catch((error)=>{
               console.log("error trying to login ",error);
               setEmailAddress('')
               setPassword('')
               setError(error.message)
           });

   }
    return(
        <>  
        <HeaderContainer>
        <Form>
        <Form.Title>Sign In</Form.Title>
          {error && <Form.Error>{error}</Form.Error>}
          <Form.Base onSubmit={handeSignin} method="POST">
             <Form.Input
             placeholder="your email address"
             type="text"
             value={emailAddress}
             onChange={({target})=>setEmailAddress(target.value)}
             />
             <Form.Input
                  type="password"
                 placeholder="your password"
                 autoComplete="off"
                 value={password}
                 onChange={({target})=>setPassword(target.value)}
             />
             <Form.Submit disabled={isInvalid} type="submit" data-testid="sign-in">
              Sign In
            </Form.Submit>

          </Form.Base>
          <Form.Text>New To Netflix? <Form.Link to={ROUTES.SIGN_UP}>sign up</Form.Link></Form.Text>
          <Form.TextSmall>
            This page is protected by Google reCAPTCHA to ensure you're not a bot. Learn more.
          </Form.TextSmall>
        </Form>
        </HeaderContainer>
        <FooterContainer/>
        </>
    )
}