using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlayerController : MonoBehaviour {

	private Rigidbody2D rb;
	private float force = 350.0f;
	public bool canJump;
	public float aye;

	void Start () {
		rb = GetComponent<Rigidbody2D>();
		canJump = true;

	}
	
	void FixedUpdate () {
		if (Input.GetButtonDown("Jump") && canJump && rb.velocity.y < 1)  {
			canJump = false;
			rb.AddForce(transform.up * force);
		}
		aye = rb.velocity.y;
	}

	void OnTriggerEnter2D(Collider2D c) {
		canJump = true;
	}
}
