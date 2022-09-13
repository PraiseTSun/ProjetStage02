import React, {useState} from 'react';
import './App.css';
import PageInscription from "./components/PageInscription";

function App() {
  const [utilisateurs, setUtilisateurs] = useState([])

  const onInscrire = async  (compte : object) => {
    const res = await fetch('http://localhost:8080/createUser',
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(compte)
        })
      const data = await res.json();
      utilisateurs.join(data)
  }
  return (
    <PageInscription onInscrire={onInscrire}/>
  );
}

export default App;
