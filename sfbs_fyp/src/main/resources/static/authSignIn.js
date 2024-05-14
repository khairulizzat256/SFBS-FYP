
//signin
const LoginForm = document.querySelector('#login-form');
LoginForm.addEventListener('submit', (e) => {
    e.preventDefault();
    const email = LoginForm['email'].value;
    const password = LoginForm['password'].value;

    console.log(email + " " + password);

    auth.signInWithEmailAndPassword(email, password).then(cred => {
        console.log(cred.user);
        LoginForm.reset();
        window.location.href = "/Admin/dashboard?uid=" + cred.user.uid;
    }).catch(err => {
        alert("Invalid email or password");
    });
});