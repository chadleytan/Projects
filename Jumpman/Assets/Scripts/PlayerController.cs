using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlayerController : MonoBehaviour {

	private Rigidbody rb;

	public bool isJumping = false;
	private float jumpHeight = 0f;
	private float jumpPeak = 100f;

	public float distToGround = 0.6f;
	public float distToWall = 0.5f;

	void Start () {
		rb = GetComponent<Rigidbody>();
	}
	
	// Update is called once per frame
	void FixedUpdate () {

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
		
		if (jumpHeight < jumpPeak) {
			rb.MovePosition(transform.position + Vector3.up * Time.deltaTime);
			jumpHeight += 1f;
		}
		else {
			isJumping = false;
			jumpHeight = 0f;
		}
	}

	void Fall() {
		rb.MovePosition(transform.position + Vector3.down * Time.deltaTime);
	}

	bool isGrounded() {
		return Physics.Raycast(transform.position, Vector3.down, distToGround);
	}

	bool isBlocked() {
		return Physics.Raycast(transform.position, Vector3.right, distToWall);
	}
}
