
import {Header, Profiles} from '../components'
import * as ROUTES from '../constants/routes';
import logo from '../logo.svg'

export function SelectProfileContainer({currentUser,setProfile,currentuseravatar}){
    console.log("loging user from seectprofilecontainer ",currentUser);
    console.log("loging currentuseravatar from seectprofilecontainer  ",currentuseravatar?.avatar);
   
    return <>
      <Header bg={false}>
      <Header.Frame>
          <Header.Logo to={ROUTES.HOME} src={logo} alt="netflix"/>
      </Header.Frame>
      </Header>
      <Profiles>
          <Profiles.Title>Who's Watching?</Profiles.Title>
          <Profiles.List>
          <Profiles.User onClick={()=>setProfile({username:currentUser.username,photoUrl:currentUser.photoUrl})}>
          <Profiles.Picture src={currentuseravatar?.avatar} />
                  <Profiles.Name>{currentUser.username}</Profiles.Name>
              </Profiles.User>
          </Profiles.List>
      </Profiles>
          </>
}