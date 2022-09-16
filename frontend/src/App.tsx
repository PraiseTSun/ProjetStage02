import React, {useState} from 'react';
import './App.css';
import PageInscription from "./components/PageInscription";

function App() {
  const [utilisateurs, setUtilisateurs] = useState([])

  const onInscrire = async  (compte : object, type : string) => {
    const res = await fetch(`http://localhost:8080/create${type}`,
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(compte)
        })

      const data = await res.json();

      if(res.status == 409){
        alert(data.error)
      }
      if(res.status == 201){
          utilisateurs.join(data)
      }
  }
  return (
    <PageInscription onInscrire={onInscrire} />
  );
}

export default App;
