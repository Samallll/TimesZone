function countDown() {
  inputElement = document.querySelector('#form1');
  value = parseInt(inputElement.value);
  if (value > 1) {
    inputElement.value = value - 1;
  }
}