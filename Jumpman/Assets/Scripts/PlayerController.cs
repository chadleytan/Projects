using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlayerController : MonoBehaviour {

	private Rigidbody rb;

	public bool isJumping = false;
	private float jumpTime = 0f;
	private float jumpPeak = 32f;
	private float speed = 4.0f;

	public float distToGround = 0.6f;
	public float distToWall = 0f;

	void Start () {
		rb = GetComponent<Rigidbody>();
	}
	

	void FixedUpdate () {

		//Pushes back player if they are blocked by the wall
		//Otherwise they catch up to original position if they were pushed back
		if (isBlocked()) {
			Blocked();
		}
		else if (!isBlocked() && rb.position.x < -6.5f){
			Catchup(); 
		}

		//Player can only jump on the ground
		if (Input.GetKeyDown("space") && isGrounded())  {
			isJumping = true;
		}

		if (isJumping) {
			Jump();
		}
		if (!isJumping && !isGrounded()){
			Fall();
		}
	}


	void Jump() {
		// Jump until Peak
		if (jumpTime < jumpPeak) {
			jumpTime += 1f;
			rb.MovePosition(transform.position + Vector3.up * Time.deltaTime * speed);
		}
		else {
			isJumping = false;
			jumpTime = 0f;
		}
	}

	void Fall() {
		rb.MovePosition(transform.position + Vector3.down * Time.deltaTime * speed);
	}

	void Blocked() {
		rb.MovePosition(transform.position + Vector3.left * Time.deltaTime * 5.0f);
	}

	void Catchup() {
		rb.MovePosition(transform.position + Vector3.right * Time.deltaTime);
	}

	// Check if player is on the ground 
	bool isGrounded() {
		return Physics.Raycast(transform.position, Vector3.down, distToGround);
	}
	//Check if player is hitting a wall
	bool isBlocked() {
		return Physics.Raycast(transform.position, Vector3.right, distToWall);
	}
}
