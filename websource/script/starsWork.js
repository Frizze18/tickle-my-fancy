var rating = 0;

for (var i = 1; i < 6; i++) {
    var star = document.getElementById("star" + i);
    star.addEventListener("click", function () {
        rating = i;
        document.getElementById("score").innerHTML="" + rating;
        changeStarRating(i);
    });
    star.addEventListener("mouseenter", function () {
        changeStarRating(i);
    });
    star.addEventListener("mouseleave", function () {
        changeStarRating(rating);
    });

}
function changeStarRating(grade) {

    for (var i = 1; i < 6; i++) {
        const star = document.getElementById("star" + i);
        if (i <= grade) {
            star.classList.add("filledstars");
        } else {
            star.classList.remove("filledstars");
        }
    }
}

