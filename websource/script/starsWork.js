var rating = 0;

/*for (var i = 1; i <= 5; i++) {
    var star = document.getElementById("star" + i);
    star.addEventListener("click", function () {
        rating = i;
        document.getElementById("score").innerText="" + rating;
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

    for (var i = 1; i <= 5; i++) {
        const star = document.getElementById("star" + i);
        if (i <= grade) {
            star.classList.add("filledstars");
        } else {
            star.classList.remove("filledstars");
        }
    }
}*/


function changeStarRating(rating){
    $(".filled").removeClass("filled");
    for(var i=1; i<=rating; i++){
        ui.stars[i-1].addClass("filled");
    }
}
var ui = {
    rating: $(".stars"),
    stars: [
        $("[data-rating-id='1']"),
        $("[data-rating-id='2']"),
        $("[data-rating-id='3']"),
        $("[data-rating-id='4']"),
        $("[data-rating-id='5']")
    ]
};
ui.rating.on("click", "span", function(e){
    var star = $(e.target);
    var rating = parseInt(star.attr("data-rating-id"));
    changeStarRating(rating);
    $("#score").val(rating);
});

