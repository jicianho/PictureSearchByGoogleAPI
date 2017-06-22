/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * Modify at your own risk.
 */

package com.google.vision.v1alpha1.model;

/**
 * A face-specific landmark (for example, a face feature). Landmark positions may fall outside the
 * bounds of the image when the face is near one or more edges of the image. Therefore it is NOT
 * guaranteed that 0 <= x < width or 0 <= y < height.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the Cloud Vision API. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class Landmark extends com.google.api.client.json.GenericJson {

  /**
   * Face landmark position.
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private Position position;

  /**
   * Face landmark type.
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String type;

  /**
   * Face landmark position.
   * @return value or {@code null} for none
   */
  public Position getPosition() {
    return position;
  }

  /**
   * Face landmark position.
   * @param position position or {@code null} for none
   */
  public Landmark setPosition(Position position) {
    this.position = position;
    return this;
  }

  /**
   * Face landmark type.
   * @return value or {@code null} for none
   */
  public java.lang.String getType() {
    return type;
  }

  /**
   * Face landmark type.
   * @param type type or {@code null} for none
   */
  public Landmark setType(java.lang.String type) {
    this.type = type;
    return this;
  }

  @Override
  public Landmark set(String fieldName, Object value) {
    return (Landmark) super.set(fieldName, value);
  }

  @Override
  public Landmark clone() {
    return (Landmark) super.clone();
  }

}
