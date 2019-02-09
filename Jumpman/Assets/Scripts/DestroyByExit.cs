using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class DestroyByExit : MonoBehaviour {

	void OnTriggerExit2D(Collider2D other) {
		Destroy(other.gameObject);
	}
}
