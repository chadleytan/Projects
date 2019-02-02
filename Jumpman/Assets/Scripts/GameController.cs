using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GameController : MonoBehaviour {

	public GameObject block;
	private GameObject nextBlock;
	public bool assignedNew = false;

	private Vector3 spawnPosition = new Vector3(13.5f, -4.0f, 0.0f);

	void Start () {
		nextBlock = GameObject.FindGameObjectWithTag("FirstBlock");
	}
	
	// Update is called once per frame
	void Update () {
		if (nextBlock.transform.position.x <= 8.5f) {
			assignedNew = true;
			nextBlock = MakeNextBlock();
		}
	}

	GameObject MakeNextBlock () {
		Quaternion spawnRotation = Quaternion.identity;
		return Instantiate(block, spawnPosition, spawnRotation);
	}
}
