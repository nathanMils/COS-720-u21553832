import React from 'react'
import './genericButton.css'

const genericButton = ({handle, text}) => 
    <button onClick = {handle}>{text}</button>;
export default genericButton