import React from 'react';

class Iframe extends React.Component{

    render(){
        return(
            <div style={{position:'relative', top:'20px', left:'0',height:'70vh'}}>
              <iframe id="inlineFrameExample"
                title="Inline Frame Example"
                style={{width:'100%', height:'100%', border:'none', margin:'0', padding:'0', overflow:'hidden', zIindex:"999999"}}
                src="http://fit-together.demo.axway.com:8443/goto/d3b931da10bcd8bb03f9849aafe94aa1?embed=true">
                </iframe>
            </div>
        )
    }
}

export default Iframe