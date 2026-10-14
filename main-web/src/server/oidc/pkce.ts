import crypto from "node:crypto"

function base64UrlEncode(buffer: Buffer) {
  return buffer
    .toString("base64")
    .replace(/\+/g, "-")
    .replace(/\//g, "_")
    .replace(/=/g, "")
}

export function generateCodeVerifier() {
  return base64UrlEncode(
    crypto.randomBytes(32)
  )
}

export function generateCodeChallenge(
  verifier: string,
) {
  return base64UrlEncode(
    crypto.createHash("sha256")
      .update(verifier)
      .digest()
  )
}

export function generateState() {
  return crypto.randomUUID()
}