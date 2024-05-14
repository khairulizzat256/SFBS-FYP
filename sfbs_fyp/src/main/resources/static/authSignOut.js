//signin
//signin
function signOut() {
    firebase.auth().signOut()
        .then(function() {
            console.log("User signed out successfully.");
            window.location.href = "/login";
        })
        .catch(function(error) {
            console.log("An error occurred while signing out:", error);
        });
}