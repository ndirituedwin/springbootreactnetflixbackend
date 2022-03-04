
import{SelectProfileContainer} from './profiles';
import { useState,useEffect } from 'react';
import { Loading,Header, Card, Player } from '../components';
import * as ROUTES from '../constants/routes'
import logo from '../logo.svg'
import { useNavigate } from 'react-router-dom';
import { FooterContainer } from './Footer';
import Fuse from 'fuse.js';
export function BrowseContainer({slides,isAuthenticated,currentUser,currentuseravatar}){

    console.log("is user authenticated from SelectProfileCntainer",isAuthenticated)
    console.log("is there a user from SelectProfileCntainer",currentUser)
    console.log("loading slides from SelectProfileCntainer",slides)
    console.log("loading current user avatar",currentuseravatar)
    const [category, setCategory] = useState('series')
      const [profile, setProfile] = useState({})
      const [loading, setLoading] = useState(true)
      const [searchTerm, setSearchTerm] = useState('')
      const [slideRows, setSlideRows] = useState([])
      const  navigate=useNavigate();
     
      useEffect(() => {
        console.log("profile",profile)
        setTimeout(()=>{setLoading(false)},3000)
  
      }, [profile.username]);
      useEffect(() => {
       setSlideRows(slides[category])
      }, [slides,category])
        console.log("slide rows ",slideRows);
    const handlesignout=()=>{
      localStorage.removeItem(ROUTES.ACCESS_TOKEN)
      localStorage.removeItem(ROUTES.EXPIRESAT)
      localStorage.removeItem(ROUTES.TOKEN_TYPE)
      localStorage.removeItem(ROUTES.USERNAMEOREMAIL)
       navigate(ROUTES.HOME)
    }
    useEffect(() => {
      const fuse = new Fuse(slideRows, { keys: ['data.description', 'data.title', 'data.genre'] });
      console.log("logging fuse ",fuse);
      const results = fuse.search(searchTerm).map(({ item }) => item);
  
      if (slideRows.length > 0 && searchTerm.length > 3 && results.length > 0) {
        setSlideRows(results);
      } else {
        setSlideRows(slides[category]);
      }
    }, [searchTerm]);
  
    // return profile.username ?(loading? <Loading src={currentUser.photourl}/>:(<Loading.ReleaseBody/>)): <SelectProfileContainer isAuthenticated={isAuthenticated} currentUser={currentUser} setProfile={setProfile} />
    return profile.username?(
      <>
      {loading? (
        <Loading src={currentuseravatar}/>
      ):(<Loading.ReleaseBody/>)}
      <Header src="joker1" dontShowOnSmallViewPort>
      <Header.Frame>
        <Header.Group>
        <Header.Logo to={ROUTES.HOME} src={logo} alt="Netflix"></Header.Logo>
        <Header.TextLink active={category==='series'?'true':'false'} onClick={()=>setCategory('series')}>Series</Header.TextLink>
        <Header.TextLink active={category==='films'?'true':'false'} onClick={()=>setCategory('films')}>Films</Header.TextLink>
        </Header.Group>

       <Header.Group>
       <Header.Search searchTerm={searchTerm} setSearchTerm={setSearchTerm}/>
          <Header.Profile>
            <Header.Picture src={currentuseravatar?.avatar}/>
             <Header.Dropdown>
            <Header.Group>
            <Header.Picture src={currentuseravatar?.avatar}/>
            <Header.TextLink>{profile.username}</Header.TextLink>  
            </Header.Group>  
             <Header.Group>
             <Header.TextLink onClick={handlesignout}>Sign out</Header.TextLink>
             </Header.Group>
            </Header.Dropdown> 
          </Header.Profile>
        </Header.Group> 
      </Header.Frame>
        <Header.Feature>
        <Header.FeatureCallOut>Watch Joker now</Header.FeatureCallOut>
        <Header.Text>
            Forever alone in a crowd, failed comedian Arthur Fleck seeks connection as he walks the streets of Gotham
            City. Arthur wears two masks -- the one he paints for his day job as a clown, and the guise he projects in a
            futile attempt to feel like he's part of the world around him.
          </Header.Text>
        <Header.PlayButton>Play</Header.PlayButton>
        </Header.Feature>
      </Header>
      <Card.Group>
      {slideRows.map((slideItem)=>(
        <Card key={`${category}-${slideItem.title.toLowerCase()}`}>
          <Card.Title>{slideItem.title}</Card.Title>
          <Card.Entities>
            {slideItem.data.map((item,docId)=>(
             <Card.Item key={docId} item={item}>
                  <Card.Image src={`/images/${category}/${item.genre}/${item.slug}/small.jpg`} />
                  <Card.Meta>
                    <Card.SubTitle>kiuytr{item.title}</Card.SubTitle>
                    <Card.Text>kytre{item.description}</Card.Text>
                  </Card.Meta>
                </Card.Item>         
            ))}
          </Card.Entities>
          <Card.Feature category={category}>
          
            <Player>
              <Player.Button></Player.Button>
              <Player.Video src="/videos/bunny.mp4"/>
            </Player>
          </Card.Feature>
        </Card>
      ))}

      </Card.Group>
      <FooterContainer/>
      </>
    ):(
      <SelectProfileContainer isAuthenticated={isAuthenticated} currentUser={currentUser} currentuseravatar={currentuseravatar} setProfile={setProfile}/>
    );
      }