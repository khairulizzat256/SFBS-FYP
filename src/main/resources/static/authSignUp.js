//signup
const SignupForm = document.querySelector('#signup-form');
SignupForm.addEventListener('submit', (e) => {
    e.preventDefault();
    const email = SignupForm['email'].value;
    const password = SignupForm['password'].value;
    const confirmPassword = SignupForm['password2'].value;

    console.log(email + " " + password + " " + confirmPassword);

    if (password == confirmPassword) {
        auth.createUserWithEmailAndPassword(email, password).then(cred => {
            console.log(cred.user);
            SignupForm.reset();
            window.location.href = "/Admin/dashboard";
        }).catch(err => {
            alert(err.message);
        });
    }
    else {
        alert("Passwords do not match");
    }
});