import React from 'react';

class Iframe extends React.Component{

    render(){
        return(
            <div style={{position:'relative', top:'20px', left:'0',height:'70vh'}}>
              <iframe id="inlineFrameExample"
                title="Inline Frame Example"
                style={{width:'100%', height:'100%', border:'none', margin:'0', padding:'0', overflow:'hidden', zIindex:"999999"}}
                src="https://search-runners-jy4ck5nrfdbtqohuhuhefdsetq.eu-west-1.es.amazonaws.com/_plugin/kibana/app/kibana?security_tenant=private#/dashboard/318f0400-7ce6-11ea-989a-87c1bc1b5c07?embed=true&_g=(refreshInterval%3A(pause%3A!t%2Cvalue%3A0)%2Ctime%3A(from%3Anow%2Fw%2Cto%3Anow%2Fw))">
                </iframe>
            </div>
        )
    }
}

export default Iframe