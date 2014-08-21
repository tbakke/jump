package com.mygdx.jump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class Input implements InputProcessor, ContactListener {

	public jump jumper;
	public float startY;
	int width, height;
	public Input (jump j){
		jumper = j;
		startY = this.jumper.dynBdy.getPosition().y;
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
	}
	
	public void handle (){
		
		Vector2 pos = this.jumper.dynBdy.getPosition();
		int screenX = Gdx.input.getX();
	//	int screenY = Gdx.input.getY();
		float impulseX = 0;
		float impulseY = 0;
		float middleX = width / 2.0f;
		float playerPosX = pos.x + middleX;
		
	//	System.out.println ("1: MouseX: " + screenX + " MouseY: " + screenY);
	//	System.out.println ("2: Pos x: " + playerPosX + " Pos Y: " + pos.y);
		
		// Haven't jumped yet so keep y = 0;
		if (pos.y <= startY && this.jumper.dynBdy.getLinearVelocity().y < 0){
			impulseY = 0;
		}
		else{
		//	System.out.println (jumper.dynBdy.getLinearVelocity().y);
			impulseY = this.jumper.dynBdy.getLinearVelocity().y;
		}
		
		
		if (playerPosX + 10 >= screenX && playerPosX - 10 <= screenX){
			impulseX = 0;
		}
		// Move left
		else if (playerPosX > screenX){

		//	impulseX = - (screenX + middleX);
			impulseX = -100;
		}
		else {
		//	impulseX = screenX - middleX;
			impulseX = 100;
		}
		
	//	System.out.println ("3: impulseX: " + impulseX );
	//	 System.out.println ("4: Vel x: " + jumper.dynBdy.getLinearVelocity().x + " Vel Y: " + jumper.dynBdy.getLinearVelocity().y);
		
		this.jumper.dynBdy.setLinearVelocity(impulseX, impulseY);
		
	//	System.out.println ("5: Vel x: " + jumper.dynBdy.getLinearVelocity().x + " Vel Y: " + jumper.dynBdy.getLinearVelocity().y);
		int i = jumper.bodies.size - 1;
		int deleteSpot = (int) - ( jumper.camera.viewportHeight * 0.2f) + 50;
		int deleteSpotCam = (int) (jumper.camera.position.y) - 100; 
	//	System.out.println (jumper.camera.position.y);
		while ( i >= 0 ){
			Body tmp = jumper.bodies.get(i);
			int tmpPos = (int) tmp.getPosition().y;
			if ( (tmpPos < deleteSpot) || (tmpPos < deleteSpotCam) ){
				jumper.deleteBodies.add(jumper.bodies.get(i));
			}
			i--;
		}
	}
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
	//	System.out.println("Down");
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
//		System.out.println("keyTyped");
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		Vector2 pos = this.jumper.dynBdy.getPosition();
		
		if (pos.y <= startY){
			
			float velocityX = 0;
			float velocityY = 75;
			float middleX = Gdx.graphics.getWidth() / 2.0f;
			
			if (pos.x > screenX - middleX){
			//	System.out.println ("Here");
			//	velocityX = -screenX;
				velocityX = - (screenX + middleX);
			}
			else {
				velocityX = screenX - middleX;
			}
			
			// Issue y velocity is being reset in handle function fix that.
			this.jumper.dynBdy.setLinearVelocity (velocityX, velocityY);
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		
//		Vector2 pos = this.jumper.dynBdy.getPosition();
		
	//	System.out.println ("x: " + screenX + " Y: " + screenY);
	//	this.jumper.dynBdy.applyLinearImpulse(screenX, screenY, pos.x, pos.y, true);
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void beginContact(Contact contact) {
		// TODO Auto-generated method stub
		
		Body notPlayer;
		if (contact.getFixtureA().getBody().equals(this.jumper.dynBdy)){
			notPlayer = contact.getFixtureB().getBody();
		}
		else {
			notPlayer = contact.getFixtureA().getBody();
		}
		if ( !notPlayer.equals(this.jumper.ground) ){
			
		
			this.jumper.deleteBodies.add(contact.getFixtureB().getBody());
		}
		/*
			Vector2 pos = this.jumper.dynBdy.getPosition();
			float velocityY = 1500;
			float velocityX = 0;
			float middleX = Gdx.graphics.getWidth() / 2.0f;
			int screenX = Gdx.input.getX();
			
			if (pos.x > screenX - middleX){
				velocityX = - (screenX + middleX);
			}
			else {
				velocityX = screenX - middleX;
			}
			
		//	System.out.println ("make sure");
		//	 System.out.println ("4: Vel x: " + jumper.dynBdy.getLinearVelocity().x + " Vel Y: " + jumper.dynBdy.getLinearVelocity().y);
			
			// this.jumper.dynBdy.setLinearVelocity (velocityX, velocityY);
		//	 jumper.dynBdy.ap
			this.jumper.dynBdy.applyLinearImpulse(0, velocityY, pos.x, pos.y, true);
		//	this.jumper.myWorld.
	//		System.out.println ("5: Vel x: " + jumper.dynBdy.getLinearVelocity().x + " Vel Y: " + jumper.dynBdy.getLinearVelocity().y);
		} */
		
	}

	@Override
	public void endContact(Contact contact) {
		
		Body notPlayer;
		if (contact.getFixtureA().getBody().equals(this.jumper.dynBdy)){
			notPlayer = contact.getFixtureB().getBody();
		}
		else if (contact.getFixtureB().getBody().equals(this.jumper.dynBdy) ) {
			notPlayer = contact.getFixtureA().getBody();
		}
		else {
			return;
		}
		if ( !notPlayer.equals(this.jumper.ground) ){
			
			
	//		this.jumper.deleteBodies.add(contact.getFixtureB().getBody());
			
			Vector2 pos = this.jumper.dynBdy.getPosition();
			float velocityY = 9050.5f;
			float velocityX = 0;
			float middleX = Gdx.graphics.getWidth() / 2.0f;
			int screenX = Gdx.input.getX();
			
			if (pos.x > screenX - middleX){
				velocityX = - (screenX + middleX);
			}
			else {
				velocityX = screenX - middleX;
			}
			
		//	System.out.println ("make sure");
			 System.out.println ("4: Vel x: " + jumper.dynBdy.getLinearVelocity().x + " Vel Y: " + jumper.dynBdy.getLinearVelocity().y);
			
		//	 this.jumper.dynBdy.setLinearVelocity (velocityX, velocityY);
			 if (jumper.dynBdy.getLinearVelocity().y < 0){
				 this.jumper.dynBdy.setLinearVelocity(velocityX, 0.0f);
			 }
			 this.jumper.dynBdy.applyLinearImpulse(0.0f, velocityY, jumper.dynBdy.getPosition().x, jumper.dynBdy.getPosition().y, true);
			System.out.println ("5: Vel x: " + jumper.dynBdy.getLinearVelocity().x + " Vel Y: " + jumper.dynBdy.getLinearVelocity().y);
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

}
