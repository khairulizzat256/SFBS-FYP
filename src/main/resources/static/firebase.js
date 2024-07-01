src="https://www.gstatic.com/firebasejs/9.6.1/firebase-app.js"
src="https://www.gstatic.com/firebasejs/9.6.1/firebase-auth.js"


const firebaseConfig = {
    apiKey: "AIzaSyBJgWo6PHesODWtMUTX2wtMcP7oaH9nhbA",
    authDomain: "sfbs-19116.firebaseapp.com",
    databaseURL: "https://sfbs-19116-default-rtdb.asia-southeast1.firebasedatabase.app",
    projectId: "sfbs-19116",
    appId: "1:745317915642:web:f456e96a3ab2fbcc0aa243",
    measurementId: "G-CDBKT1GQGV"
};

// Initialize Firebase
firebase.initializeApp(firebaseConfig);

const auth = firebase.auth();