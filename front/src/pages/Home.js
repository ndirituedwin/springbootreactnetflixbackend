import JumbotronContainer from './../containers/Jumbotron';
import { FaqsConntainer } from './../containers/Faqs';
import { FooterContainer } from './../containers/Footer';
import { HeaderContainer } from '../containers/header';
import { OptForm,Feature } from '../components';

export default function Home(){

    return<>
    <HeaderContainer>
    <Feature>
        <Feature.Title>
            Unlimited films,Tv Programmes and More
        </Feature.Title>
        <Feature.SubTitle>
            Watch anywhere,Cancel anyTime
        </Feature.SubTitle>
    <OptForm>
        <OptForm.Input placeholder="Email address" />
        <OptForm.Button>Try it now</OptForm.Button>
        <OptForm.Break />
        <OptForm.Text>Ready to watch? Enter your email to create or restart your membership.</OptForm.Text>
      </OptForm>
    <OptForm/>
    </Feature>
    </HeaderContainer>
    <JumbotronContainer /> <FaqsConntainer /> <FooterContainer/>
    </>
}