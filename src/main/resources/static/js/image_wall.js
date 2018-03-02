
$(document).ready(function() {
  console.log($('#grid'));
  $('#grid').mediaBoxes({
    boxesToLoadStart: 10,
    boxesToLoad: 8,
  });
});