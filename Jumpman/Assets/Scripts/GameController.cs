using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GameController : MonoBehaviour {

	public GameObject[] blocks;
	private GameObject nextBlock;
	private GameObject prevBlock;


	private Vector3 spawnPosition = new Vector3(13.5f, -4.0f, 0.0f);

	void Start () {
		nextBlock = GameObject.FindGameObjectWithTag("FirstBlock");
	}
	
	// Update is called once per frame
	void FixedUpdate () {
		if (nextBlock.transform.position.x <= 8.75f) {
			nextBlock = MakeNextBlock();
			Debug.Log("Block Name: "+ nextBlock.name);
		}
	}

	GameObject MakeNextBlock () {
		GameObject block = ChooseNextBlock();
		Quaternion spawnRotation = Quaternion.identity;
		return Instantiate(block, spawnPosition, spawnRotation);
	}

	GameObject [] GenerateNextChoices() {
		

		if (nextBlock.name.Contains("LowBlock")) {
			GameObject [] nextChoices = new GameObject [3];
			nextChoices[0] = blocks[0];
			nextChoices[1] = blocks[1];
			nextChoices[2] = blocks[3];

			return nextChoices;
		}
		// Special case for NoBlock because height of next block must be equal or less
		else if (nextBlock.name.Contains("NoBlock")) {
			if (prevBlock.name.Contains("LowBlock")) {
				GameObject [] nextChoices = new GameObject [1];
				nextChoices[0] = blocks[0];

				return nextChoices;
			}
			else if (prevBlock.name.Contains("MidBlock")) {
				GameObject [] nextChoices = new GameObject [2];
				nextChoices[0] = blocks[0];
				nextChoices[1] = blocks[1];

				return nextChoices;
			}
			else {
				GameObject [] nextChoices = new GameObject [3];
				nextChoices[0] = blocks[0];
				nextChoices[1] = blocks[1];
				nextChoices[2] = blocks[2];

				return nextChoices;
			}
		}
		return blocks;
	}

	GameObject ChooseNextBlock() {
		GameObject nextChoice;
		// Randomly choose the next possible block

		GameObject [] nextChoices = GenerateNextChoices();
		nextChoice = nextChoices[Random.Range(0, nextChoices.Length)];

		// Record previous block for next iteration
		if (nextChoice.name.Contains("NoBlock")) {
			prevBlock = nextBlock;
		}
		
		return nextChoice;
	
	}
}
