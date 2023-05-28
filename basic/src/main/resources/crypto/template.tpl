{
  "meta": {
    "uuid": SLOT_PROTOTYPE_PROPS>LOGICAL_SLOT_UID,
    "provider": {
      "uuid": SLOT_PROTOTYPE_PROPS>CRYPTO_PROVIDER_UID
    },
    "contentRestriction": {
      "type": SLOT_PROTOTYPE_PROPS>OBJECT_TYPE,
      "algId": SLOT_PROTOTYPE_PROPS>ALG_ID,
      "isExportable": SLOT_PROTOTYPE_PROPS>EXPORTABILITY
    },
    "capacity": SLOT_PROTOTYPE_PROPS>SLOT_CAPACITY,
    "dependency": {
        "couid": {
            "generator": {
                "uuid": SLOT_PROTOTYPE_PROPS>DEPENDENCY_COUID_GEN_UUID
            },
            "version": SLOT_PROTOTYPE_PROPS>DEPENDENCY_COUID_VERSION
        },
        "type": SLOT_PROTOTYPE_PROPS>DEPENDENCY_TYPE
    }
    "owner": {
      "uuid": SLOT_PROTOTYPE_PROPS>OWNER_UID
    },
    "versionControl": {
        "type": SLOT_PROTOTYPE_PROPS>VERSION_CONTROL,
        "prevContent": {
            "couid": {
                "generator": {
                    "uuid": SLOT_PROTOTYPE_PROPS>VERSION_TRACK>GENERATOR_UID
                },
                "version": SLOT_PROTOTYPE_PROPS>VERSION_TRACK>VERSION_STAMP
            }
        }
    }
  },
  "content": {
    "meta": {
      "couid": {
        "generator": {
          "uuid": SLOT_CONTENT_PROPS>OBJECT_UID>GENERATOR_UID
        },
        "version": SLOT_CONTENT_PROPS>OBJECT_UID>VERSION_STAMP
      }
      "type": SLOT_PAYLOAD>KEY_TYPE,
      "algId": SLOT_CONTENT_PROPS>ALG_ID,
      "bitSize": SLOT_PAYLOAD>KEY_BIT_LENGTH,
      "allowedUsage": 0
    },
    "content": {
      "e": "SLOT_PAYLOAD>KEY_EXPONENT",
      "d": "SLOT_PAYLOAD>KEY_EXPONENT",
      "modulus": "SLOT_PAYLOAD>KEY_MODULUS",
      "x": "SLOT_PAYLOAD>KEY_X_PUBLIC",
      "y": "SLOT_PAYLOAD>KEY_Y_PUBLIC",
      "d": "SLOT_PAYLOAD>PRIVATE_KEY"
    }
  }
}
