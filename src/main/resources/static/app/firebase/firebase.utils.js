import firebase from 'firebase/app';
import 'firebase/database';
import 'firebase/auth';

const config ={
  apiKey: "AIzaSyDI9qTg-Hqaq6IV3YxZVyLZlTqCUPN877A",
  authDomain: "runners-ca664.firebaseapp.com",
  databaseURL: "https://runners-ca664.firebaseio.com",
  projectId: "runners-ca664",
  storageBucket: "runners-ca664.appspot.com",
  messagingSenderId: "853470073085",
  appId: "1:853470073085:web:9552f5c210f2f813776e94",
  measurementId: "G-ND44FC6PPV"

  };

  firebase.initializeApp(config);

  export const auth=firebase.auth();
  export const database = firebase.database()
  var provider = new firebase.auth.GoogleAuthProvider();
  provider.setCustomParameters({prompt:'select_account'});
  export const signInWithGoogle = () => auth.signInWithPopup(provider);
  export default firebase;