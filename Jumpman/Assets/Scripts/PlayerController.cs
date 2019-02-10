using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlayerController : MonoBehaviour {

	private GameController gameController;

	private Rigidbody2D rb;
	private float force = 325.0f;
	public bool canJump;
	public float aye;

	void Start () {
		rb = GetComponent<Rigidbody2D>();
		canJump = true;

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
			if (Input.GetButtonDown("Jump") && canJump && rb.velocity.y < 1 && rb.velocity.y > -1)  {
				canJump = false;
				rb.AddForce(transform.up * force);
			}
		}
		aye = rb.velocity.y;
	}

	void OnTriggerEnter2D(Collider2D c) {
		canJump = true;
	}
}
