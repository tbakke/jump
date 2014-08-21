package com.mygdx.jump;

import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

// import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class jump extends ApplicationAdapter {
	
	SpriteBatch batch;
	// Texture img;
	World myWorld;
	
	Body dynBdy;
//	Body blk;
//	Body styBdy;
	Body ground;
	
	// bodies player jumps on
	Array<Body> bodies;
	
	//bodies to delete
	Array<Body> deleteBodies;
	
	Input handler;	

	OrthographicCamera camera;
//	private SpriteBatch batch;
	Box2DDebugRenderer debugRenderer;

	int bodyWidth;
	int bodyHeight;
	
	@Override
	public void create () {
		
		BodyDef myBodyDef;
		PolygonShape shape;
	//	FixtureDef fixDef;
		FixtureDef dynFix;
		
		
		batch = new SpriteBatch();
	//	img = new Texture("badlogic.jpg");
		
		// Create world
		myWorld = new World (new Vector2 (0, -35), true);
		
		// Init stuff
		myBodyDef = new BodyDef();
		shape = new PolygonShape ();
	//	fixDef = new FixtureDef ();
		dynFix = new FixtureDef ();
		deleteBodies = new Array<Body>();
		bodies = new Array <Body>();
		// make camera
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		System.out.println ("Width: " + Gdx.graphics.getWidth() + " " + "Height: " + Gdx.graphics.getHeight());
		System.out.println ("Width: " +camera.viewportWidth + " " + "Height: " + camera.viewportHeight);
		
		// init debugRenderer
		debugRenderer = new Box2DDebugRenderer ();
		
		// The dynamicBody Body Def
		myBodyDef.type = BodyType.DynamicBody;
		myBodyDef.position.set (-200, -(camera.viewportHeight * 0.2f) + 24);

		myBodyDef.angle = 0;
		
		// Create DynBody
		dynBdy = myWorld.createBody(myBodyDef);
		
		// Create the shape for the dynamic body
		shape.setAsBox(2, 10);
		dynFix.shape = shape;
		dynFix.density = 1;

		// create the fixture
		dynBdy.createFixture(dynFix);
		
		shape.dispose();
		
	/*	// Static Body 
		BodyDef myBodyDef2 = new BodyDef();
		
		myBodyDef2.type = BodyType.StaticBody;
		myBodyDef2.position.set (0,5);
		styBdy = myWorld.createBody (myBodyDef2);
		
		shape = new PolygonShape ();
		
		shape.setAsBox (25, 25);
		fixDef.shape = shape;
		styBdy.createFixture (fixDef);
		
		shape.dispose(); 
		
		myBodyDef2.type = BodyType.StaticBody;
		myBodyDef2.position.set (35,125);
		blk = myWorld.createBody (myBodyDef2);
		
		shape = new PolygonShape ();
		
		shape.setAsBox (25, 25);
		fixDef.shape = shape;
		blk.createFixture (fixDef);
		
		shape.dispose();  */
		
		// Ground
		shape = new PolygonShape();
		
		BodyDef def3 = new BodyDef();
		def3.type = BodyType.StaticBody;
	//	System.out.println(Gdx.graphics.getHeight());

		def3.position.set(new Vector2 (0, -(camera.viewportHeight * 0.2f) ));

		shape.setAsBox(camera.viewportWidth, 10.f);
		
		ground = myWorld.createBody(def3);
		
		ground.createFixture(shape, 0.0f);
		
		shape.dispose();
		
		handler = new Input (this);
		
		Gdx.input.setInputProcessor(handler);
		
		myWorld.setContactListener(handler);
		
		bodyWidth = 10;
		bodyHeight = 10;
		
		int i = 0;
		while (i < 10 ){
			initCreateBody ();
			i++;
		}
	
	}

	@Override
	public void render () {
		
		moveCamera();
		
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		debugRenderer.render(myWorld, camera.combined);
		
	//	batch.begin();
	//	batch.draw(img, 0, 0);
	//	batch.end();
		
		
		handler.handle();
		
		
		
		myWorld.step(1/60f, 6, 2);
	
		if (deleteBodies.size >= 1){
			while (deleteBodies.size > 0){
				Body del = deleteBodies.pop();
				bodies.removeValue(del, false);
				myWorld.destroyBody(del);
				
				
			}
		}
		deleteBodies.clear();
		if (bodies.size < 6){
			int i = 0;
			while (i < 15 ){
				createBody ();
				i++;
			}
		}
		
	}
	
	private void moveCamera (){
		
		if (dynBdy.getPosition().y > (camera.viewportHeight / 4.0f) ){

			camera.position.set(camera.position.x, dynBdy.getPosition().y, 0);
			camera.update();

		}
	}
	
	private void createBody (){
		
		BodyDef def = new BodyDef();
		FixtureDef fixDef = new FixtureDef();
		PolygonShape shape = new PolygonShape ();
		Body tmpBody;
		int posX, posY;
		Random rand = new Random();
		
		
		// Get the max and min x and y for random num gen
		int maxY = ( (int) ( camera.viewportHeight ) + (int) camera.position.y );
		int minY = (int) ( camera.viewportHeight / 2.0f ) + bodyHeight + (int) camera.position.y;
		
		int maxX = ( (int) ( camera.viewportWidth / 2.0f ) ) - bodyWidth;
	//	int minX = - maxX;
		
		// Make this better!
	//	posY = rand.nextInt((int) ( ( (camera.viewportHeight + 55) - (camera.viewportHeight + 25)  + 1 ) + camera.viewportHeight + 25) );
	//	posX = rand.nextInt((int) ( ( (camera.viewportWidth) + 1)));
		
		
		// Randomly generate where the body will spawn
		posY = rand.nextInt ( ((maxY - minY) + 1) ) + minY;

		posX = (rand.nextInt(maxX*2) - maxX);
		
	//	System.out.println ("Pos X: " + posX + " Pos Y: " + posY);
		
		// create body
		def.type = BodyType.DynamicBody;
		def.position.set (posX,posY);
		def.angle = 0;
		
		tmpBody = myWorld.createBody (def);

		shape = new PolygonShape ();
		
		shape.setAsBox (bodyWidth, bodyHeight);
		fixDef.shape = shape;
		fixDef.density = 1;
		tmpBody.createFixture (fixDef);
		tmpBody.setLinearDamping(0.5f);
		
		bodies.add(tmpBody);
		shape.dispose(); 
		
	}
private void initCreateBody (){
		
		BodyDef def = new BodyDef();
		FixtureDef fixDef = new FixtureDef();
		PolygonShape shape = new PolygonShape ();
		Body tmpBody;
		int posX, posY;
		Random rand = new Random();
		
		
		// Get the max and min x and y for random num gen
		int maxY = (int) ( camera.viewportHeight / 2.0f );
		int minY = bodyHeight * 4;
		
		int maxX = ( (int) ( camera.viewportWidth / 2.0f ) ) - bodyWidth;
	//	int minX = - maxX;
		
		// Make this better!
	//	posY = rand.nextInt((int) ( ( (camera.viewportHeight + 55) - (camera.viewportHeight + 25)  + 1 ) + camera.viewportHeight + 25) );
	//	posX = rand.nextInt((int) ( ( (camera.viewportWidth) + 1)));
		
		
		// Randomly generate where the body will spawn
		posY = rand.nextInt ( ((maxY - minY) + 1) ) + minY;

		posX = (rand.nextInt(maxX*2) - maxX);
		
	//	System.out.println ("Pos X: " + posX + " Pos Y: " + posY);
		
		// create body
		def.type = BodyType.DynamicBody;
		def.position.set (posX,posY);
		def.angle = 0;
		
		tmpBody = myWorld.createBody (def);
		
		shape = new PolygonShape ();
		
		shape.setAsBox (bodyWidth, bodyHeight);
		fixDef.shape = shape;
		fixDef.density = 1;
		tmpBody.createFixture (fixDef);
		tmpBody.setLinearDamping(0.5f);
		bodies.add(tmpBody);
		shape.dispose(); 
		
	}
	
}
