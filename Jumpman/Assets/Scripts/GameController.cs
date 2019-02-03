using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GameController : MonoBehaviour {

	public GameObject[] blocks;
	private GameObject nextBlock;


	private Vector3 spawnPosition = new Vector3(13.5f, -4.0f, 0.0f);

	void Start () {
		nextBlock = GameObject.FindGameObjectWithTag("FirstBlock");
	}
	
	// Update is called once per frame
	void FixedUpdate () {
		if (nextBlock.transform.position.x <= 8.75f) {
			nextBlock = MakeNextBlock();
		}
	}

	GameObject MakeNextBlock () {
		GameObject block = blocks[Random.Range(0, blocks.Length)];
		Quaternion spawnRotation = Quaternion.identity;
		return Instantiate(block, spawnPosition, spawnRotation);
	}
}
