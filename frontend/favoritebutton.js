var globalElement;

window.provideElement = function provideElement(element) {
   globalElement = element;
}

window.initFW = (id) => {
  const button = document.getElementById('favorite-button'+id);
  
  if (button == null || button.getAttribute('listener') === 'true') {
     return;
  }
  else {
	  button.setAttribute('listener', 'true');
  }
  
  const star = document.getElementById('star'+id);
  const firework = document.getElementById('firework'+id);

  button.addEventListener('click', () => {
    const isActive = button.classList.toggle('active');
    star.innerHTML = isActive ? '&#9733;' : '&#9734;'; // Gefüllter oder umrandeter Stern
	globalElement.$server.toggleFavorite(id, isActive);

    if (isActive) {
    // Entfernen Sie die Animation, um sie zurückzusetzen
      firework.style.animation = 'none';
      firework.offsetHeight; // Trigger reflow
      firework.style.animation = ''; // Setzen Sie die Animation wieder zurück
      // Entfernen Sie die Klasse "active", um sie zurückzusetzen
      firework.classList.remove('active');
      // Forcieren Sie ein Reflow, um sicherzustellen, dass die Änderung wirksam wird
      void firework.offsetWidth;
      // Fügen Sie die Klasse "active" hinzu, um die Animation zu starten
      firework.classList.add('active');
    }
	else {
		// Entfernen Sie die Klasse "active", um sie zurückzusetzen
      firework.classList.remove('active');
      // Forcieren Sie ein Reflow, um sicherzustellen, dass die Änderung wirksam wird
      void firework.offsetWidth;
	}
  });
}

