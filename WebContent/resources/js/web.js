/**
 * 
 */

let createPopup = (popInfo) => {
		let positionX = screen.width/2 - popInfo.width/2;
		let positionY = screen.height/2 - popInfo.height/2;
		
		let popup = open(popInfo.url, popupInfo.name, `width=${popInfo.width}
													, height=${popInfo.height},top=${positionX}, left = ${positionY}`)
		
		return popup;
	};