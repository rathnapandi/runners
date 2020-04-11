import React from 'react';
import './FormInput.style.css';
const FormInput=({handleChange,label,...otherProps})=>{
	return(
		<div className="div-FormInput">
			<input className="input"
			onChange={handleChange}
			{...otherProps}/>
		</div>
	)
}
export default FormInput;