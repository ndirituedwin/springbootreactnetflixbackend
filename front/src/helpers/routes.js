 
import { Route, Navigate } from 'react-router-dom';


 export function IsUserRedirect({user,loggedInPath,children,...rest}){
    console.log("user in IsUserRedirect ",user)
    console.log("loggedInPath in IsUserRedirect ",loggedInPath)
    console.log("children in IsUserRedirect ",children)
    // console.log("rest in IsUserRedirect ",rest)

    return(
        <Route
            {...rest}
            render={()=>{
                if(!user){
                    console.log("The user is not authenticated",user)
                    return children;
                }
            if(user){
                console.log("the user is authenticated",user)
                return (
                    <Navigate to={{loggedInPath}}/>
                )
              
            }
            return null;
            }}
        />
    )
 }
 export function ProtectedRoute({ isAuthenticated, children, ...rest }) {
    return (
      <Route
        {...rest}
        render={({ location }) => {
          if (isAuthenticated) {
            return children;
          }
  
          if (!isAuthenticated) {
            return (
              <Redirect
                to={{
                  pathname: '/signin',
                  state: { from: location },
                }}
              />
            );
          }
  
          return null;
        }}
      />
    );
  }