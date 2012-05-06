package org.ironchefpython.rhinodemo;

import java.io.IOException;

import org.ironchefpython.modapi.JsModManager;
import org.ironchefpython.modapi.ModRegistry;
import org.junit.Test;


public class ModTest {
	
	public static void main(String[] args) throws IOException {
		new ModTest().mockTest();
	}
	
	@Test
	public void mockTest() throws IOException {


		ModRegistry registry = new ModRegistry();
		JsModManager mm = new JsModManager(registry, "test");

		mm.runScript(ModTest.class.getResourceAsStream("testmod.js"), "testmod.js");
		
//		mm.exec("game.addEventListener('test', function(evt) { console.log(evt.button) }, false)");

//		ArgumentCaptor<EventListener> argument = ArgumentCaptor.forClass(EventListener.class);
//		verify(game).addEventListener(anyString(), argument.capture(), eq(false));
//
//		MouseEvent uiEvent= mock(MouseEvent.class);
//		when(uiEvent.getType()).thenReturn("mouseevent");
//		when(uiEvent.getButton()).thenReturn((short) 1);
//		argument.getValue().handleEvent(uiEvent);
	}
}
