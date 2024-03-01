import {
    BrowserRouter,
    Route,
    Routes
} from 'react-router-dom'
import {
    Home,
    Login
} from './pages'
import {
    useEffect,
    useState
} from 'react'
import './App.css'

function App() {
    const [loggedIn, setLoggedIn] = useState(false)
    const [email, setEmail] = useState('')

    return (
        <div className='App'>
            <BrowserRouter>
                <Routes>
                    <Route path='/' element={<Home email={email} loggedIn={loggedIn} setLoggedIn={setLoggedIn}/>} ></Route>
                    <Route path='/login' element={<Login setLoggedIn={setLoggedIn} setEmail={setEmail}/>} ></Route>
                </Routes>
            </BrowserRouter>
        </div>
    )
}

export default App