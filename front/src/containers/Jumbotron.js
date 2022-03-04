

import jumbodata from '../fixtures/jumbo'
import {Jumbotron} from './../components';
export default function JumbotronContainer() {
  console.log(jumbodata);
  return (
    <Jumbotron.Container>
      {jumbodata.map((item)=>(
        <Jumbotron key={item.id} direction={item.direction}>
         <Jumbotron.Pane>
         <Jumbotron.Title>{item.title}</Jumbotron.Title>
        <Jumbotron.SubTitle>{item.subTitle}</Jumbotron.SubTitle>

         </Jumbotron.Pane>
         <Jumbotron.Pane>
          <Jumbotron.Image src={item.image} alt={item.alt}/>
          </Jumbotron.Pane>   
        </Jumbotron>
      ))}
    </Jumbotron.Container>
  );
}
