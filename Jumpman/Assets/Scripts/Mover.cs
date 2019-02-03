using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Mover : MonoBehaviour {

	private Rigidbody rb;
	public float speed;

	// Use this for initialization
	void Start () {
		rb = GetComponent<Rigidbody>();
	}

	void FixedUpdate () {
		rb.MovePosition(transform.position + Vector3.left * Time.deltaTime * speed);
	}


}
