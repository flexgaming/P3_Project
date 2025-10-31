import { useState } from "react";
import reactLogo from "./assets/react.svg";
import viteLogo from "/vite.svg";
import "./App.css";

function App() {
    const [count, setCount] = useState(0);
    const [count2, setCount2] = useState(0);

    return (
        <>
            <div>
                <a href="https://vite.dev" target="_blank">
                    <img src={viteLogo} className="logo" alt="Vite logo" />
                </a>
                <a href="http://localhost:8080/dockers" target="_blank">
                    <img
                        src={reactLogo}
                        className="logo react"
                        alt="React logo"
                    />
                </a>
            </div>
            <h1>Vite + localhost:8080/dockers</h1>
            <div className="card">
                <button
                    onClick={() => {
                        setCount((count) => count + 1);
                        setCount2((count2) => count2 + 1);
                    }}
                >
                    count is {count}
                </button>
                <p>
                    Edit <code>src/App.jsx</code> and save to test HMR
                </p>
                <div>
                    <p>
                        Hello World! This is a sample React application using
                        Vite as the build tool.
                    </p>
                    <button onClick={() => setCount2((count2) => count2 + 1)}>
                        Buttons clicked {count2} times!
                    </button>
                </div>
            </div>
            <p className="read-the-docs">
                Click on the Vite and React logos to learn more
            </p>
        </>
    );
}

export default App;
