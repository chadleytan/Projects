using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Mover : MonoBehaviour {

	private float speed = 5;

	private GameController gameController;

	void Start ()
	{
		GameObject gameControllerObject = GameObject.FindWithTag ("GameController");
		if (gameControllerObject != null)
		{
			gameController = gameControllerObject.GetComponent <GameController>();
		}
		if (gameController == null){
			Debug.Log ("Cannot find 'GameController' script");
		}
	}

	void FixedUpdate () {
		if (!gameController.getGameStatus()) {
			transform.position += Vector3.left * Time.deltaTime * speed;
		}
	}


}
