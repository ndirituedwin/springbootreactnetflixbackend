
import react,{ useState,useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { HeaderContainer } from './../containers/header';
import { FooterContainer } from './../containers/Footer';
import { Form } from '../components';
import * as ROUTES from '../constants/routes'
import { signup as SignUp } from './../constants/ApiUtils';
export default function Signup({ isAuthenticated}){
    const navigate=useNavigate()
    const [name, setName] = useState('')
    const [username, setUsername] = useState('')
    const [emailAddress, setEmailAddress] = useState('')
    const [password, setPassword] = useState('')
    const [error, setError] = useState('')
    const isInvalid = name==='' || password === '' || emailAddress === '';
    useEffect(() => {
        if(isAuthenticated){
            navigate(ROUTES.APISIGNUPURL)
        }
    }, [])
    const signup=(event)=>{
        event.preventDefault();
        const signupdata={
            name:name,
            username:username,
            email:emailAddress,
            password:password
        }

        SignUp(signupdata).then((response)=>{
            console.log("logging response after signup {}",response)
            navigate(ROUTES.SIGN_IN)
        }).catch((error)=>{
            console.log("error occurred during signup",error);
            setName('')
            setUsername('')
            setEmailAddress('')
            setPassword('')
            setError(error.message)
        })
    }

    return(
        <>
        <HeaderContainer>
            <Form>
                <Form.Title>Sign up</Form.Title>
                {error && <Form.Error>{error}</Form.Error>}
                <Form.Base onSubmit={signup} method="POST">
                    <Form.Input 
                     type="text"
                     onChange={({target})=>setName(target.value)}
                    value={name} 
                    placeholder="your full name"
                     />

                      <Form.Input 
                     type="text"
                     onChange={({target})=>setUsername(target.value)}
                    value={username} 
                    placeholder="your username "
                     />
                      <Form.Input 
                     type="text"
                     onChange={({target})=>setEmailAddress(target.value)}
                    value={emailAddress} 
                    placeholder="your email address"
                     />
                       <Form.Input 
                     type="password"
                     onChange={({target})=>setPassword(target.value)}
                    value={password} 
                    autoComplete="off"
                    placeholder="your password"
                     />
                     <Form.Submit
                      type="submit"
                      disable={isInvalid}>Sign Up</Form.Submit>
                </Form.Base>
                <Form.Text> Already a user?  <Form.Link to={ROUTES.SIGN_IN}>sign in now</Form.Link></Form.Text>
          <Form.TextSmall>
          This page is protected by Google reCAPTCHA to ensure you're not a bot. Learn more.
          </Form.TextSmall>
            </Form>
        </HeaderContainer>
        <FooterContainer/>
        </>
    )
}