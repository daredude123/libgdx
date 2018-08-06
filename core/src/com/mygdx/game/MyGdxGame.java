package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class MyGdxGame extends ApplicationAdapter {
	public Environment environment;
	public CameraInputController cameraInputController;
	AssetManager assetManager;
	ModelBuilder modelBuilder;
	ModelBatch modelBatch;
	Texture img;
	Model model;
	ModelInstance modelInstance;
	public boolean loading;

	public PerspectiveCamera perspectiveCamera;
	private Array<ModelInstance> instances = new Array<ModelInstance>();

	@Override
	public void create () {
		modelBatch = new ModelBatch();
		modelBuilder = new ModelBuilder();
		assetManager = new AssetManager();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));


		perspectiveCamera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		perspectiveCamera.position.set(10f, 10f, 10f);
		perspectiveCamera.lookAt(0,0,0);
		perspectiveCamera.near = 1f;
		perspectiveCamera.far = 300f;
		perspectiveCamera.update();
//
//		model = modelBuilder.createBox(5f,5f,5f, new Material(ColorAttribute.createDiffuse(Color.GREEN)),
//				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

		assetManager.load("data/untitled.g3db",Model.class);
		assetManager.finishLoading();
		cameraInputController = new CameraInputController(perspectiveCamera);
		Gdx.input.setInputProcessor(cameraInputController);
		loading = true;
	}


	@Override
	public void render () {
		if (loading) {
			doneLoading();
		}
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(perspectiveCamera);
		modelBatch.render(instances,environment);
		modelBatch.end();
	}
	
	@Override
	public void dispose () {
		modelBatch.dispose();
		instances.clear();
		assetManager.dispose();
	}

	private void doneLoading() {
		Model square = modelBuilder.createBox(5f,5f,5f, new Material(ColorAttribute.createDiffuse(Color.GREEN)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
		Model tree = assetManager.get("data/untitled.g3db",Model.class);
		for (float x = -24f; x <= 24f; x += 6f) {
			for (float z = -24f; z <= 24f; z += 6f) {
				ModelInstance squareInstance = new ModelInstance(tree);
				squareInstance.transform.setToTranslation(x, 0, z);
				instances.add(squareInstance);
			}
		}
		ModelInstance modelInstance = new ModelInstance(square);
		instances.add(modelInstance);
		loading = false;
	}
}
