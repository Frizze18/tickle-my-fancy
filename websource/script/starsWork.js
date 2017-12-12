var rating = 0;

for (var i = 1; i <= 5; i++) {
    var star = document.getElementById("" + i);
    star.addEventListener("click", function () {
        rating = parseInt(this.id, 10);
        document.getElementById("score").value= rating;
        changeStarRating(rating);
    });
    star.addEventListener("mouseenter", function (e) {
        changeStarRating(parseInt((this.id), 10));
    });
    star.addEventListener("mouseleave", function () {
        changeStarRating(rating);
    });

}
function changeStarRating(grade) {

    for (var i = 1; i <= 5; i++) {
        const star = document.getElementById("" + i);
        if (i <= grade) {
            star.classList.add("filled");
        } else {
            star.classList.remove("filled");
        }
    }
}

