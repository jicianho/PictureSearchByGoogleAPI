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
 * Set of features pertaining to the image, computed by various computer vision methods over safe-
 * search verticals (for example, adult, spoof, medical, violence).
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the Cloud Vision API. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class SafeSearchAnnotation extends com.google.api.client.json.GenericJson {

  /**
   * Represents the adult contents likelihood for the image.
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String adult;

  /**
   * Likelihood this is a medical image.
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String medical;

  /**
   * Spoof likelihood. The likelihood that an obvious modification was made to the image's canonical
   * version to make it appear funny or offensive.
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String spoof;

  /**
   * Violence likelihood.
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String violence;

  /**
   * Represents the adult contents likelihood for the image.
   * @return value or {@code null} for none
   */
  public java.lang.String getAdult() {
    return adult;
  }

  /**
   * Represents the adult contents likelihood for the image.
   * @param adult adult or {@code null} for none
   */
  public SafeSearchAnnotation setAdult(java.lang.String adult) {
    this.adult = adult;
    return this;
  }

  /**
   * Likelihood this is a medical image.
   * @return value or {@code null} for none
   */
  public java.lang.String getMedical() {
    return medical;
  }

  /**
   * Likelihood this is a medical image.
   * @param medical medical or {@code null} for none
   */
  public SafeSearchAnnotation setMedical(java.lang.String medical) {
    this.medical = medical;
    return this;
  }

  /**
   * Spoof likelihood. The likelihood that an obvious modification was made to the image's canonical
   * version to make it appear funny or offensive.
   * @return value or {@code null} for none
   */
  public java.lang.String getSpoof() {
    return spoof;
  }

  /**
   * Spoof likelihood. The likelihood that an obvious modification was made to the image's canonical
   * version to make it appear funny or offensive.
   * @param spoof spoof or {@code null} for none
   */
  public SafeSearchAnnotation setSpoof(java.lang.String spoof) {
    this.spoof = spoof;
    return this;
  }

  /**
   * Violence likelihood.
   * @return value or {@code null} for none
   */
  public java.lang.String getViolence() {
    return violence;
  }

  /**
   * Violence likelihood.
   * @param violence violence or {@code null} for none
   */
  public SafeSearchAnnotation setViolence(java.lang.String violence) {
    this.violence = violence;
    return this;
  }

  @Override
  public SafeSearchAnnotation set(String fieldName, Object value) {
    return (SafeSearchAnnotation) super.set(fieldName, value);
  }

  @Override
  public SafeSearchAnnotation clone() {
    return (SafeSearchAnnotation) super.clone();
  }

}
