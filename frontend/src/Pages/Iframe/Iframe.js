import React from 'react';

class Iframe extends React.Component{

    render(){
        return(
            <div style={{position:'relative', top:'20px', left:'0',height:'70vh'}}>
              <iframe id="inlineFrameExample"
                title="Inline Frame Example"
                style={{width:'100%', height:'100%', border:'none', margin:'0', padding:'0', overflow:'hidden', zIindex:"999999"}}
                src="https://fit-together.demo.axway.com:8443/app/kibana#/dashboard/be91a680-7e19-11ea-bb27-95c6cc06702e?embed=true&_g=(refreshInterval%3A(pause%3A!t%2Cvalue%3A0)%2Ctime%3A(from%3Anow%2Fw%2Cto%3Anow%2Fw))">
                </iframe>
            </div>
        )
    }
}

export default Iframe