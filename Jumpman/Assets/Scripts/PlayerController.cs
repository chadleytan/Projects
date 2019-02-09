using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlayerController : MonoBehaviour {

	private Rigidbody2D rb;
	private float force = 350.0f;
	public bool isJumping;

	void Start () {
		rb = GetComponent<Rigidbody2D>();
		isJumping = false;
	}
	
	void FixedUpdate () {
		if (Input.GetButtonDown("Jump") && !isJumping)  {
			isJumping = true;
			rb.AddForce(transform.up * force);
		}
	}

	void OnTriggerEnter2D(Collider2D c) {
		isJumping = false;
	}
}
