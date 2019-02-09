using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Mover : MonoBehaviour {

	private float speed = 5;

	void FixedUpdate () {
		transform.position += Vector3.left * Time.deltaTime * speed;
	}


}
